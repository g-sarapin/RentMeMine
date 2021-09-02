package business.services.admin_service;

import business.dto.MessageBoxDTO;
import business.dto.PersonUserFullDTO;
import business.dto.Spam;
import org.springframework.transaction.annotation.Transactional;

public interface IAdminService {
    @Transactional
    String setPersonBlocked(String id, boolean blocked);

    String setAnnouncementBlocked(long id, boolean blocked);

    PersonUserFullDTO getFullProfile(String id);

    @Transactional
    String setLessor(String id);

    MessageBoxDTO getMessageBox(long ig);

    String sendSpam(Spam spam);
}