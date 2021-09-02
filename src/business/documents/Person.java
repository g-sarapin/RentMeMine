package business.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@NoArgsConstructor
@Getter
@Document(collection = "persons")
public class Person {

    @Id
    private String login;
    private boolean lessor = false;
    private String about;
    private String eMail;
    private String name;
    private Map<String, String> phones;
    private CharSequence photo;
    private boolean blocked = false;
    private Set<Long> favorites = new HashSet<>();
    private Set<Long> announcements = new HashSet<>();

    Set<Long> messageBoxIds = new HashSet<>();
    Set<Long> unreadMessageBoxes = new HashSet<>();

//    Set<RentRecord> RentRecords;


    public Person(String login, String about, String eMail, String name,
                  Map<String, String> phones, CharSequence photo) {
        this.login = login;
        this.about = about;
        this.eMail = eMail;
        this.name = name;
        this.phones = phones;
        this.photo = photo;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhones(Map<String, String> phones) {
        this.phones = phones;
    }

    public void setPhoto(CharSequence photo) {
        this.photo = photo;
    }

    public void setMessageBoxIds(Set<Long> messageBoxIds) {
        this.messageBoxIds = messageBoxIds;
    }

    public void setUnreadMessageBoxes(Set<Long> unreadMessageBoxes) {
        this.unreadMessageBoxes = unreadMessageBoxes;
    }

    public void setFavorites(Set<Long> favorites) {
        this.favorites = favorites;
    }

    public void setAnnouncements(Set<Long> announcements) {
        this.announcements = announcements;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isLessor() {
        return lessor;
    }

    public void setLessor(boolean lessor) {
        this.lessor = lessor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(login, person.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}