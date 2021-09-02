package business.services.admin_service;

import business.documents.Announcement;
import business.documents.EmailRecord;
import business.documents.Person;
import business.dto.Mapper;
import business.dto.MessageBoxDTO;
import business.dto.PersonUserFullDTO;
import business.dto.Spam;
import business.exceptions.DataBaseException;
import business.repositories.EmailRepository;
import business.services.account_service.IAccountService;
import business.services.announcement_service.IAnnouncementService;
import business.services.email_service.EmailService;
import business.services.messageBox_service.IMessageBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.security_logic.controllers.SecurityController;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    @Autowired
    IAccountService accServ;

    @Autowired
    IAnnouncementService annServ;

    @Autowired
    IMessageBoxService msbServ;

    @Autowired
    EmailRepository emailRepo;

    @Autowired
    EmailService emailServ;

    @Autowired
    SecurityController secCtrl;

    @Override
    @Transactional
    public String setPersonBlocked(String id, boolean blocked){
        StringBuilder result = new StringBuilder("Ok");
        Person person = accServ.getPerson(id, null, false, false);
        if (person.isBlocked() == blocked)
            throw new DataBaseException("Profile already" + (blocked ? " blocked!" : " unblocked!"));
        person.setBlocked(blocked);
        try{
            person.getAnnouncements().forEach(a -> setAnnouncementBlocked(a, blocked));
        } catch (RuntimeException e) {
            if (result.toString().equals("Ok"))
            {
                result.append("\n Next problems was found :");
                result.append("\n" + e.getMessage());
            }
        }
        accServ.savePerson(person);
        return result.toString();
    }

    @Override
    public String setAnnouncementBlocked(long id, boolean blocked){
        Announcement announcement = annServ.getAnnouncement(id, null, true, false);
        if(announcement.isBlocked() == blocked)
            throw new DataBaseException("Announcement already" + (blocked ? " blocked!" : " unblocked!"));
        announcement.setBlocked(blocked);
        annServ.saveAnnouncement(announcement);
        return "Ok";
    }

    @Override
    public PersonUserFullDTO getFullProfile(String id){
        Person person = accServ.getPerson(id, null, false, false);
        return  person.isLessor() ? Mapper.getPersonLessorFullDTO(person) : Mapper.getPersonUserFullDTO(person);
    }

    @Override
    @Transactional
    public String setLessor(String id) {
        Person person = accServ.getPerson(id, null, true, false);
        person.setLessor(true);
        accServ.savePerson(person);
        secCtrl.grantRole(id, "LESSOR");
        return "Ok";
    }

    @Override
    public MessageBoxDTO getMessageBox(long id){
        return msbServ.getMessageBox(null, id);
    }

    @Override
    public String sendSpam(Spam spam) {
        List<EmailRecord> eMails = emailRepo.findAll();
        eMails.forEach(e -> emailServ.sendSimpleEmail(e.eMail, spam.title, spam.text));
        return "Messages sent";
    }
}