package security.security_logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import security.security_logic.dto.Mapper;
import security.security_logic.entities.Account;

@Component
public class UserLoader implements UserDetailsService{

    @Autowired
    @Qualifier("securityMongoTemplate")
    MongoTemplate accountRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Account account = accountRepo.findById(login, Account.class);
        if(account == null) throw new UsernameNotFoundException("Login not registered");
        return new User(login,
                account.getPassword(),
                AuthorityUtils.createAuthorityList(Mapper.RoleToAuthority(account.getRoles())));
    }

}