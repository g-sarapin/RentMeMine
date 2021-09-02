package business.repositories;

import business.documents.Announcement;
import business.documents.PriceCounter;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;

public interface AnnouncementRepository extends MongoRepository<Announcement, Long> {

    @Async
    @Aggregation(pipeline = {"?0", "{$group: {'_id': 'null', cities: {$addToSet: '$city'}}}", "{$unwind: '$cities'}"})
    Future<String[]> getCities(Document matcher);

    @Async
    @Aggregation(pipeline = {"?0", "{$group: {'_id': '$priceGroup', num: { $sum: 1}}}"})
    Future<List<PriceCounter>> getPrices(Document matcher);

    @Async
    @Aggregation(pipeline = {"?0", "{$group: {'_id': null, types: {$addToSet: '$type'}}}", "{$unwind: '$types'}"})
    Future<String[]> getTypes(Document matcher);

    @Async
    @Aggregation(pipeline = {"?0", "{$group: {'_id': null, rooms: {$addToSet: $numBedroom}}}", "{$unwind: '$rooms'}"})
    Future<int[]> getRooms(Document matcher);

    @Async
    @Aggregation(pipeline = {"?0", "{$unwind: '$amenities'}", "{$group: {'_id': null, 'amenits': { '$addToSet': '$amenities'}}}}", "{$unwind: '$amenits'}"})
    Future<String[]> getAmenities(Document matcher);

    @Async
    @Aggregation(pipeline = {"?0", "{$count: qty}}}"})
    Future<int[]> getQuantity(Document matcher);

    @Aggregation(pipeline = {"?0", "?1", "?2", "?3"})
    List<Announcement> getAnnouncements(Document matcher, Document sort, Document skip, Document limit);
}