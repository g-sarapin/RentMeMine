package business.repositories;

import business.documents.Pictures;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PicturesRepository extends MongoRepository<Pictures, Long> {

}