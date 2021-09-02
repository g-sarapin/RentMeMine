package business.services.user_service;

import business.documents.Announcement;
import business.documents.Person;
import business.dto.*;
import business.exceptions.DataBaseException;
import business.repositories.PicturesRepository;
import business.services.account_service.IAccountService;
import business.services.announcement_service.IAnnouncementService;
import business.services.messageBox_service.IMessageBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.security_logic.services.ISecurityService;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    IAccountService accServ;

    @Autowired
    IAnnouncementService annServ;

    @Autowired
    PicturesRepository picRepo;

    @Autowired
    IMessageBoxService msgServ;

    @Autowired
    ISecurityService secServ;
    //*
    @Override
    public PersonUserFullDTO getOwnProfile(String secId, String login) {
        Person person = accServ.getPerson(login, secId, false, false);
        return  person.isLessor() ? Mapper.getPersonLessorFullDTO(person) : Mapper.getPersonUserFullDTO(person);
    }
    //8
    @Override
    public PersonUserFullDTO setProfile(String secId, PersonUserFullDTO profileDTO) {
        Person profile = accServ.getPerson(profileDTO.login, secId, true, true);
        if (profile == null) profile = new Person(profileDTO.login, null, profileDTO.eMail, profileDTO.name,
                profileDTO.phones, profileDTO.photo);
        else {
            profile.setName(profileDTO.name);
            profile.seteMail(profileDTO.eMail);
            profile.setPhoto(profileDTO.photo);
            profile.setPhones(profileDTO.phones);
            if (profile.isLessor()) profile.setAbout(((PersonLessorFullDTO) profileDTO).about);
        }
        validatePerson(profile);
        profile = accServ.savePerson(profile);
        return  profile.isLessor() ? Mapper.getPersonLessorFullDTO(profile) : Mapper.getPersonUserFullDTO(profile);
    }
    //*
    @Override
    public MessageBoxDTO sendMessage(String secId, MessageDTO message) {
        return msgServ.sendMessage(secId, message);
    }
    //*
    @Override
    public MessageBoxDTO getMessageBox(String secId, long boxId) {
        return msgServ.getMessageBox(secId, boxId);
    }

    @Override
    public List<MessageBoxDTO> getMessageBoxes(String secId, List<Long> boxIds) {
        return msgServ.getMessageBoxes(secId, boxIds);
    }

    @Override
    public String removeAccount(String secId, String login, String token) {
        StringBuilder result = new StringBuilder("Ok");
        Person person = accServ.getPerson(login, secId, false, false);
        try{
            person.getAnnouncements().forEach(id -> removeAnnouncement(secId,id));
        } catch (Exception e) {
            if (e.getClass() == DataBaseException.class) {
                if (result.toString().equals("Ok")) result.append("\n Next problems was found :");
                result.append("\n" + e.getMessage());
            } else throw e;
        }
        accServ.removePerson(login);
        secServ.removeAccount(secId);
        secServ.signOut(token);
        return result.toString();
    }

    @Override
    public PersonLessorFullDTO removeAnnouncement(String secId, long id){
        Person person = accServ.getPerson(secId, null , true, false);
        Announcement announcement = annServ.getAnnouncement(id, secId, true, false);           announcement.setDeleted(true);
        announcement.setOwnerID(null);
        person.getAnnouncements().remove(id);
        person = accServ.savePerson(person);
        annServ.saveAnnouncement(announcement);
        return Mapper.getPersonLessorFullDTO(person);
    }

    @Override
    @Transactional
    public PersonUserFullDTO modifyFavorites(String login, boolean add, long id) {
        Person person = accServ.getPerson(login,null, true, false);
        if (add) person.getFavorites().add(id);
        else person.getFavorites().remove(id);
        person = accServ.savePerson(person);
        return person.isLessor() ? Mapper.getPersonLessorFullDTO(person) : Mapper.getPersonUserFullDTO(person);
    }

    @Override
    public void validatePerson(Person newProfile){
    }
}