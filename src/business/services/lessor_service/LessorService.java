package business.services.lessor_service;

import business.documents.Announcement;
import business.documents.Person;
import business.documents.Pictures;
import business.dto.AnnouncementDTO;
import business.dto.Mapper;
import business.dto.PicturesDTO;
import business.exceptions.ValidationException;
import business.repositories.PicturesRepository;
import business.services.account_service.IAccountService;
import business.services.announcement_service.IAnnouncementService;
import business.services.search_service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class LessorService implements ILessorService {

    @Autowired
    IAccountService accServ;

    @Autowired
    IAnnouncementService annServ;

    @Autowired
    PicturesRepository picRepo;

    @Autowired
    ISearchService searchServ;

    @Override
    @Transactional
    public AnnouncementDTO setAnnouncement(String secId, AnnouncementDTO announcementDTO) {
        Person person = accServ.getPerson(announcementDTO.ownerID, secId, true, false);
        if ( announcementDTO.id != null ) annServ.getAnnouncement(announcementDTO.id, secId, true, true);
        else {
            announcementDTO.id = annServ.getNewId();
            person.getAnnouncements().add(announcementDTO.id);
        }
        Announcement announcement = Mapper.getAnnouncement(announcementDTO);
        validateAnnouncement(announcement);
        announcement = annServ.saveAnnouncement(announcement);
        accServ.savePerson(person);
        return Mapper.getAnnouncementDTO(announcement);
    }

    @Override
    @Transactional
    public PicturesDTO setPictures(String secId, PicturesDTO photos){
        Pictures pictures = Mapper.getPictures(photos);
        annServ.getAnnouncement(photos.id, secId, true, true);
        pictures = picRepo.save(pictures);
        return Mapper.getPicturesDTO(pictures);
    }

    @Override
    public AnnouncementDTO setAnnouncementActive(String secId, long id, boolean active){
        Announcement announcement = annServ.getAnnouncement(id, secId, true, true);
        announcement.setActive(active);
        announcement = annServ.saveAnnouncement(announcement);
        return Mapper.getAnnouncementDTO(announcement);
    }

    @Override
    public AnnouncementDTO modifyExpirationDate(String secId, long id, String date){
        Announcement announcement = annServ.getAnnouncement(id, secId, true, true);
        long newDate = 0;
        newDate = LocalDate.parse(date).toEpochDay();
        if (newDate < LocalDate.now().toEpochDay()) throw new ValidationException("Date not valid!");
        announcement.setExpirationDate(newDate);
        announcement = annServ.saveAnnouncement(announcement);
        return Mapper.getAnnouncementDTO(announcement);
    }

    private void validateAnnouncement (Announcement newAnnouncement){
    }
}