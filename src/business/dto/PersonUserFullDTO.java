package business.dto;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class PersonUserFullDTO {

    public String login;
    public String password;
    public String eMail;
    public String name;
    public CharSequence photo;
    public boolean blocked;
    public Map<String, String> phones;
    public Set<Long> favorites;
    public List<Long> messageBoxIds;
    public List<Long> unreadMessageBoxes;

    public PersonUserFullDTO(String login, String eMail, String name, CharSequence photo, boolean blocked, Map<String,
            String> phones, Set<Long> favorites, List<Long> messageBoxIds, List<Long> unreadMessageBoxes) {
        this.login = login;
        this.eMail = eMail;
        this.name = name;
        this.photo = photo;
        this.blocked = blocked;
        this.phones = phones;
        this.favorites = favorites;
        this.messageBoxIds = messageBoxIds;
        this.unreadMessageBoxes = unreadMessageBoxes;
    }
}