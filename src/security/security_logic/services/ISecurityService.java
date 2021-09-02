package security.security_logic.services;

import business.dto.PersonUserFullDTO;
import security.security_logic.dto.AccountDto;
import org.springframework.http.ResponseEntity;

public interface ISecurityService {

    ResponseEntity<?> signUp(String login, String password, PersonUserFullDTO profile);
    ResponseEntity<?> signIn(String login, String password);
    AccountDto signOut(String token);

    AccountDto removeAccount(String login);

    AccountDto grantRole(String login, String role);
    AccountDto depriveRole(String login, String role);

    ResponseEntity<?> refreshToken();
}