package business.services.search_service;

import business.repositories.AnnouncementRepository;
import business.documents.Announcement;
import business.documents.PriceCounter;
import business.dto.DropDownRequestDTO;
import business.dto.DropDownResponseDTO;
import business.dto.FindRequestDTO;
import business.exceptions.ValidationException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.Future;

@Service
public class SearchService implements ISearchService {

    @Autowired
    AnnouncementRepository repo;

    @Override
    public DropDownResponseDTO getResponse(DropDownRequestDTO request){

        MatcherBuilder cityMatcher = new MatcherBuilder();
        MatcherBuilder priceMatcher = new MatcherBuilder();
        MatcherBuilder typeMatcher = new MatcherBuilder();
        MatcherBuilder roomsMatcher = new MatcherBuilder();
        MatcherBuilder amenitiesMatcher = new MatcherBuilder();
        MatcherBuilder quantityMatcher = new MatcherBuilder();

        if (request.city != null) {
            priceMatcher.addCityMatcher(request.city);
            typeMatcher.addCityMatcher(request.city);
            roomsMatcher.addCityMatcher(request.city);
            amenitiesMatcher.addCityMatcher(request.city);
            quantityMatcher.addCityMatcher(request.city);
        }

        if (request.priceFrom != null || request.priceTo != null){
            cityMatcher.addPriceMatcher(request.priceFrom, request.priceTo);
            typeMatcher.addPriceMatcher(request.priceFrom, request.priceTo);
            roomsMatcher.addPriceMatcher(request.priceFrom, request.priceTo);
            amenitiesMatcher.addPriceMatcher(request.priceFrom, request.priceTo);
            quantityMatcher.addPriceMatcher(request.priceFrom, request.priceTo);
        }

        if (request.type != null){
            cityMatcher.addTypeMatcher(request.type);
            priceMatcher.addTypeMatcher(request.type);
            roomsMatcher.addTypeMatcher(request.type);
            amenitiesMatcher.addTypeMatcher(request.type);
            quantityMatcher.addTypeMatcher(request.type);
        }

        if (request.rooms != null){
            cityMatcher.addRoomsMatcher(request.rooms);
            priceMatcher.addRoomsMatcher(request.rooms);
            typeMatcher.addRoomsMatcher(request.rooms);
            amenitiesMatcher.addRoomsMatcher(request.rooms);
            quantityMatcher.addRoomsMatcher(request.rooms);
        }

        if (request.amenities != null){
            cityMatcher.addAmenitiesMatcher(request.amenities);
            priceMatcher.addAmenitiesMatcher(request.amenities);
            typeMatcher.addAmenitiesMatcher(request.amenities);
            roomsMatcher.addAmenitiesMatcher(request.amenities);
            quantityMatcher.addAmenitiesMatcher(request.amenities);
        }

        if (request.availableFrom != null){
            cityMatcher.addAvailableFromMatcher(request.availableFrom);
            priceMatcher.addAvailableFromMatcher(request.availableFrom);
            typeMatcher.addAvailableFromMatcher(request.availableFrom);
            roomsMatcher.addAvailableFromMatcher(request.availableFrom);
            amenitiesMatcher.addAvailableFromMatcher(request.availableFrom);
            quantityMatcher.addAvailableFromMatcher(request.availableFrom);
        }

        long start = System.currentTimeMillis();

        Future<String[]> citiesF = repo.getCities(cityMatcher.buildMatcher());
        Future<List<PriceCounter>> pricesF = repo.getPrices(priceMatcher.buildMatcher());
        Future<String[]> typesF = repo.getTypes(typeMatcher.buildMatcher());
        Future<int[]> roomsF = repo.getRooms(roomsMatcher.buildMatcher());
        Future<String[]> amenitiesF = repo.getAmenities(amenitiesMatcher.buildMatcher());
        Future<int[]> quantityF = repo.getQuantity(quantityMatcher.buildMatcher());

        while(!citiesF.isDone() && !pricesF.isDone() && !typesF.isDone() && !roomsF.isDone() && !amenitiesF.isDone() && !quantityF.isDone()){}

        String[] cities, types, amenities;
        Map<Integer, Integer> prices = new TreeMap<>();
        int[] rooms, quantity;

        try{
            cities = citiesF.get();
            pricesF.get().forEach(pr -> prices.put(pr.price, pr.num));
            types = typesF.get();
            rooms = roomsF.get();
            amenities = amenitiesF.get();
            quantity = quantityF.get();
        } catch (Exception e) {throw new RuntimeException(e.getMessage());}

        System.out.println("Responce time: " + (System.currentTimeMillis() - start));

        return new DropDownResponseDTO(cities, prices, types, rooms, amenities, quantity.length == 0 ? 0 : quantity[0]);
    }

    @Override
    public String[] getCities() {
        String[] result;
        try{
            result = repo.getCities(new MatcherBuilder().buildMatcher()).get();
        } catch (Exception e){ throw new RuntimeException(e.getMessage());}
        return result;
    }

    @Override
    public List<Announcement> getAnnouncements(FindRequestDTO findRequest) {
        MatcherBuilder matcher = new MatcherBuilder();
        if (findRequest.city != null) matcher.addCityMatcher(findRequest.city);
        if (findRequest.priceFrom != null || findRequest.priceTo != null)
            matcher.addPriceMatcher(findRequest.priceFrom, findRequest.priceTo);
        if (findRequest.rooms != null) matcher.addRoomsMatcher(findRequest.rooms);
        if (findRequest.type != null) matcher.addTypeMatcher(findRequest.type);
        if (findRequest.availableFrom != null) matcher.addAvailableFromMatcher(findRequest.availableFrom);
        if (findRequest.amenities != null) matcher.addAmenitiesMatcher(findRequest.amenities);
        Document match = matcher.buildMatcher();
        Document sort = new Document("$sort", getSort(findRequest.sortedBy != null ? findRequest.sortedBy : ""));
        Document skip = new Document("$skip", findRequest.from);
        Document limit = new Document("$limit", findRequest.to - findRequest.from);
        return  repo.getAnnouncements(match, sort, skip, limit);
    }

    private class MatcherBuilder {

        private Document matcher= new Document();

        public MatcherBuilder addCityMatcher(String city){
            matcher.append("city",city);
            return this;
        }

        public MatcherBuilder addPriceMatcher(Integer from, Integer to){
            if (to == null ){
                matcher.append("price", new Document("$gte",from));
                return this;
            }
            if (from == null ){
                matcher.append("price", new Document("$lte", to));
                return  this;
            }
            matcher.append("price", new Document("$gte",from).append("$lte", to));
            return this;
        }

        public MatcherBuilder addTypeMatcher(String[] types){
            ArrayList<Document> documents = new ArrayList<>();
            for (int i = 0 ; i < types.length ; i++) documents.add(new Document("type", types[i]));
            matcher.append("$or", documents);
            return this;
        }

        public MatcherBuilder addUnavailableDatesMatcher(String dateFrom, String dateTo){
            long from = Long.MIN_VALUE, to = Long.MAX_VALUE;
            try{
                from = LocalDate.parse(dateFrom).toEpochDay();
                to = LocalDate.parse(dateTo).toEpochDay();
            } catch (DateTimeParseException e){ throw new ValidationException("Invalid date format!");}
            matcher.append("unavailableDays", new Document("$elemMatch", new Document("$gte", from).append("$lte", to)));
            return this;
        }

        public MatcherBuilder addAvailableFromMatcher(String dateFrom){
            long from = Long.MIN_VALUE;
            try{
                from = LocalDate.parse(dateFrom).toEpochDay();
            } catch (DateTimeParseException e){ throw new ValidationException("Invalid date format!");}
            matcher.append("availableFrom", new Document("$lte", from));
            return this;
        }

        public MatcherBuilder addRoomsMatcher(Integer[] rooms){
            ArrayList<Document> documents = new ArrayList<>();
            for (int i = 0 ; i < rooms.length ; i++) documents.add(new Document("numBedroom", rooms[i]));
            matcher.append("$or", documents);
            return this;
        }

        public MatcherBuilder addAmenitiesMatcher(String[] amenities){
            matcher.append("amenities", new Document("$all", Arrays.asList(amenities)));
            return this;
        }

        public Document buildMatcher() {
            return new Document("$match", matcher);
        }
    }

    private Document getSort(String sortMethod){
        switch (sortMethod){
            case "PriceDown" : return new Document("price", -1);
            case "SquareUp" : return new Document("totalSquare", 1);
            case "SquareDown" : return new Document("totalSquare", -1);
            case "RoomsUp" : return new Document("numBedroom", 1);
            case "RoomsDown" : return new Document("numBedroom", -1);
            case "AvailableUp" : return new Document("availableFrom", 1);
            case "AvailableDown" : return new Document("availableFrom", -1);
            default : return new Document("price", 1);
        }
    }
}