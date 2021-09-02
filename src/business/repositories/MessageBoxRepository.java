package business.repositories;

import business.documents.MessageBox;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageBoxRepository extends MongoRepository<MessageBox, Long> {

}