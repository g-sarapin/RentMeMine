package business.repositories;

import business.documents.EmailRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<EmailRecord, String> {

}