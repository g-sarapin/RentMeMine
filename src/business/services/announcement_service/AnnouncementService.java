package business.services.announcement_service;

import business.documents.Announcement;
import business.documents.AnnouncementArchive;
import business.exceptions.DataBaseException;
import business.repositories.AnnouncementArchiveRepository;
import business.repositories.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class AnnouncementService implements IAnnouncementService {

    @Autowired
    AnnouncementRepository repo;

    @Autowired
    @Qualifier("businessMongoTemplate")
    MongoTemplate templ;

    @Autowired
    AnnouncementArchiveRepository arcRepo;

    @Override
    @Transactional
    public Announcement getAnnouncement(long id, String secId, boolean delChk, boolean blkChk){
        Announcement announcement = checkAnnouncementDates(repo.findById(id).orElseGet(() -> (Announcement) arcRepo.findById(id)
                .orElseThrow(() -> new DataBaseException("Announcement " + id + " not found"))));
        if (secId != null && !secId.equals(announcement.getOwnerID())) throw new SecurityException("Recipient is not an owner of this announcement, access denied!");
        if (delChk && announcement.isDeleted()) throw new SecurityException("Announcement already deleted, access denied!");
        if (blkChk && announcement.isBlocked()) throw new SecurityException("Announcement blocked, access denied!");
        return announcement;
    }

    @Override
    @Transactional
    public List<Announcement> getAnnouncements(List<Long> ids){
        ArrayList<Announcement> announcements = (ArrayList<Announcement>) repo.findAllById(ids);
        if (announcements.size() < ids.size()) {
            List<AnnouncementArchive> annArch = (List<AnnouncementArchive>)arcRepo.findAllById(ids);
            annArch.forEach(ann -> announcements.add((Announcement)ann));
        }
        for (int i = 0 ; i < announcements.size() ; i++ ){
            Announcement announcement = announcements.get(i);
            announcements.set(i, checkAnnouncementDates(announcement));
        }
        return announcements;
    }

    @Override
    @Transactional
    public Announcement checkAnnouncementDates(Announcement announcement){
        long nowLong = LocalDate.now().toEpochDay();
        boolean toSave = false;
        if (announcement.isActive() && announcement.getExpirationDate() < nowLong) {
            announcement.setActive(false);
            toSave = true;
        }
        if (announcement.getUnavailableDays().length > 0 && announcement.getUnavailableDays()[0] < nowLong) {
            SortedSet<Long> listDays = new TreeSet(Arrays.asList(announcement.getUnavailableDays())).tailSet(nowLong);
            announcement.setUnavaibleDays(listDays.toArray(new Long[0]));
            toSave = true;
        }
        if (toSave) announcement = saveAnnouncement(announcement);
        return announcement;
    }

    @Override
    @Transactional
    public Announcement saveAnnouncement(Announcement announcement) {
        if (announcement.isActive() && !announcement.isDeleted() && !announcement.isBlocked()) {
            announcement = repo.save(announcement);
            arcRepo.deleteById(announcement.getId());
        } else {
            announcement = arcRepo.save(announcement);
            repo.deleteById(announcement.getId());
        }
        return announcement;
    }

    @Override
    public long getNewId() {
        return repo.count() + arcRepo.count() + 1;
    }
}