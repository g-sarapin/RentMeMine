package business.repositories;

import business.documents.AnnouncementArchive;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnnouncementArchiveRepository extends MongoRepository<AnnouncementArchive, Long> {

}