package security.security_logic.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class AccountDto {
    private String login;
    private String roles;
}