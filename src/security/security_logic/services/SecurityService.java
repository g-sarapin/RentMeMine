package security.security_logic.services;

import business.dto.PersonUserFullDTO;
import business.services.user_service.IUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import security.config.Roles;
import security.security_logic.dto.AccountDto;
import security.security_logic.dto.Mapper;
import security.security_logic.entities.Account;
import security.security_logic.entities.BlockedToken;
import security.security_logic.jwt.JWTDto;
import security.security_logic.jwt.JWTTokenUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class SecurityService implements ISecurityService {

    @Autowired
    @Qualifier(value="securityMongoTemplate")
    MongoTemplate securityRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JWTTokenUtil jwtTokenUtil;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    IUserService userServ;

    @Override
    public ResponseEntity<?>  signUp(String login, String password, PersonUserFullDTO profile) {
        Account account = new Account(login, encoder.encode(password), Mapper.StringToRole("USER"));

        if (securityRepo.findById(login, Account.class) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicated login " + login);
        }
        securityRepo.save(account);
        try{
            userServ.setProfile(login, profile);
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            login,
                            password));
            String token = jwtTokenUtil.generateToken(authentication);
            Map<String, Object> response = new HashMap<>();
            response.put("jwtToken", token);
            response.put("userDetails", profile);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            securityRepo.remove(account);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> signIn(String login, String password) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            login,
                            password));
            String token = jwtTokenUtil.generateToken(authentication);
            PersonUserFullDTO person = userServ.getOwnProfile(jwtTokenUtil.getUsernameFromToken(token), login);
            Map<String, Object> response = new HashMap<>();
            response.put("jwtToken", token);
            response.put("userDetails", person);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong login-password pair", e);
        }
    }

    @Override
    public AccountDto signOut(String token) {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        User user = null;
        try {
            user = (User) securityContext.getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new SecurityException("logout exception. No one has logged on current session");
        }

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(true);
        logoutHandler.setClearAuthentication(true);

        BlockedToken blockedToken = new BlockedToken(token);
        securityRepo.save(blockedToken);

        Roles role = user.getAuthorities().stream()
                .map(grantedAuthority -> Mapper.authorityToRole(grantedAuthority.getAuthority())).findFirst().orElse(null);

        return Mapper.accountDto(new Account(user.getUsername(), user.getPassword(),role));
    }

    @Override
    public AccountDto removeAccount(String login) {
        Account account = getAccount(login);
        try {
            securityRepo.remove(account);
        } catch (Exception e) {
            throw new SecurityException("Unexpected error occurred, connect our support team");
        }
        return Mapper.accountDto(account);
    }

    @Override
    public AccountDto grantRole(String login, String newRole) {
        if (newRole.equalsIgnoreCase("ADMIN"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access denied");

        Account account = getAccount(login);
        account.setRoles(Mapper.StringToRole(newRole));

        securityRepo.save(account);
        return Mapper.accountDto(account);
    }

    @Override
    public AccountDto depriveRole(String login, String role) {
        return null;
    }

    @Override
    public ResponseEntity<?> refreshToken() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication user = securityContext.getAuthentication();
        String newToken = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new JWTDto(newToken));
    }

    private Account getAccount(String login) {
        Account account = securityRepo.findById(login , Account.class);
        if (account == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login " + login + " not found");
        return account;
    }
}