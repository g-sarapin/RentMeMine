package security.security_logic.entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import security.config.Roles;

import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "accounts")
public class Account {
    @Id
    private String login;
    private String password;
    private Roles roles;
}