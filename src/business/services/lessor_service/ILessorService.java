package business.services.lessor_service;

import business.dto.AnnouncementDTO;
import business.dto.PicturesDTO;
import org.springframework.transaction.annotation.Transactional;

public interface ILessorService {
    @Transactional
    AnnouncementDTO setAnnouncement(String secId, AnnouncementDTO announcementDTO);

    @Transactional
    PicturesDTO setPictures(String secId, PicturesDTO photos);

    AnnouncementDTO setAnnouncementActive(String secId, long id, boolean active);

    AnnouncementDTO modifyExpirationDate(String secId, long id, String date);
}