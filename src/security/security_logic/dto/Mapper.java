package security.security_logic.dto;

import security.config.Roles;
import security.security_logic.entities.Account;

public class Mapper {
    public static AccountDto accountDto(Account account) {
        return new AccountDto(account.getLogin(), account.getRoles().name());
    }

    public static Roles StringToRole(String name) {
        return Roles.valueOf(name);
    }

    public static String roleToString(Roles role) {
        return role.name();
    }

    public static String RoleToAuthority(Roles role) {
        return "ROLE_" + Mapper.roleToString(role);
    }

    public static Roles authorityToRole(String authority) {
        return Mapper.StringToRole(authority.substring(5));
    }
}