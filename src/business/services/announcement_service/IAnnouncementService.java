package business.services.announcement_service;

import business.documents.Announcement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAnnouncementService {
    Announcement getAnnouncement(long id, String secId, boolean delChk, boolean blkChk);

    List<Announcement> getAnnouncements(List<Long> ids);//, String secId, boolean delChk, boolean blkChk);

    Announcement checkAnnouncementDates(Announcement announcement);

    @Transactional
    Announcement saveAnnouncement(Announcement announcement);

    long getNewId();
}