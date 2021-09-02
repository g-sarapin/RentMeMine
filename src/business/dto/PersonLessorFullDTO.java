package business.dto;

import lombok.NoArgsConstructor;
import java.util.*;

@NoArgsConstructor
public class PersonLessorFullDTO extends PersonUserFullDTO{

    public String about;
    public Set<Long> announcements = new HashSet<>();

    public PersonLessorFullDTO(String login, String eMail, String name, CharSequence photo, boolean blocked,
                               Map<String, String> phones, Set<Long> favorites, List<Long> messageBoxIds,
                               List<Long> unreadMessageBoxes, String about, Set<Long> announcements) {
        super(login, eMail, name, photo, blocked, phones, favorites,messageBoxIds, unreadMessageBoxes);
        this.about = about;
        this.announcements = announcements;
    }
}