package business.documents;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;

@Document(collection = "announcements")
public class Announcement extends AnnouncementArchive{

    public Announcement(long id, double latitude, double longitude, String country, String city, String street,
                        int building, PropertyType type, double totalSquare, int numBedroom, int numBathroom,
                        int floor, CharSequence preview, Set<Amenities> amenities, String ownerID, String title,
                        String text, int cancellation, int minDurationDays, double securityDeposit,
                        long availableFrom, long[] unavailableDays, double price, boolean active, boolean blocked,
                        boolean deleted, long expirationDate) {
        super(id, latitude, longitude, country, city, street, building, type, totalSquare, numBedroom, numBathroom,
                floor, preview, amenities, ownerID, title, text, cancellation, minDurationDays, securityDeposit,
                availableFrom, unavailableDays, price, active, blocked, deleted, expirationDate);
    }
}