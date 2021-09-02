package business.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class PersonLessorDTO {
    public String login;
    public String about;
    public String name;
    public CharSequence photo;
    public Set<Long> announcements = new HashSet<>();
}