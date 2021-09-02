package security.security_logic.controllers;

import business.dto.PersonUserFullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.config.SECURITY_API;
import security.security_logic.dto.AccountDto;
import security.security_logic.dto.AuthPair;
import security.security_logic.jwt.JWTDto;
import security.security_logic.services.ISecurityService;

@RestController
@RequestMapping(SECURITY_API.SECURITY)
@CrossOrigin
public class SecurityController {

    @Autowired
    ISecurityService service;

    @PostMapping(SECURITY_API.SIGN_UP)
    public ResponseEntity<?> addUser(@RequestBody PersonUserFullDTO profile) {
        return service.signUp(profile.login, profile.password, profile);
    }

    @PostMapping(SECURITY_API.SIGN_IN)
    public ResponseEntity<?> addAccount(@RequestBody AuthPair authPair) {
        return service.signIn(authPair.getLogin(), authPair.getPassword());
    }

    @PostMapping(SECURITY_API.SIGN_OUT)
    public AccountDto signOut(@RequestBody JWTDto token) {
        return service.signOut(token.getJwttoken());
    }

    @PostMapping(SECURITY_API.GRANT_ROLE)
    public AccountDto grantRole(String login, String role) {
        return service.grantRole(login, role);
    }

    @DeleteMapping(SECURITY_API.REMOVE_ACCOUNT)
    public AccountDto removeUser(String login) {
        return service.removeAccount(login);
    }

    @GetMapping(SECURITY_API.REFRESH_TOKEN)
    public ResponseEntity<?> refreshToken() {
        return service.refreshToken();
    }
}