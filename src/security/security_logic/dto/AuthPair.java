package security.security_logic.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class AuthPair {
    private String login;
    private String password;
}