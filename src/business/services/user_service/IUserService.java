package business.services.user_service;

import business.documents.Person;
import business.dto.MessageBoxDTO;
import business.dto.MessageDTO;
import business.dto.PersonLessorFullDTO;
import business.dto.PersonUserFullDTO;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface IUserService {
    PersonUserFullDTO getOwnProfile(String secId, String login);

    PersonUserFullDTO setProfile(String secId, PersonUserFullDTO profileDTO);

    MessageBoxDTO sendMessage(String secId, MessageDTO message);

    MessageBoxDTO getMessageBox(String secId, long boxId);

    List<MessageBoxDTO> getMessageBoxes(String secId, List<Long> boxIds);

    String removeAccount(String secId, String login, String authHeader);

    PersonLessorFullDTO removeAnnouncement(String secId, long id);

    @Transactional
    PersonUserFullDTO modifyFavorites(String login, boolean add, long id);

    void validatePerson(Person newProfile);

}