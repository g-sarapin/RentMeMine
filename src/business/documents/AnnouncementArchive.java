package business.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Document(collection = "annArchive")
public class AnnouncementArchive {

    @Id
    private long id;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String street;
    private int building;
    private PropertyType type;
    private double totalSquare;
    private int numBedroom;
    private int numBathroom;
    private int floor;
    private CharSequence preview;

    private Set<Amenities> amenities;

    private String ownerID;
    private String title;
    private String text;
    private int cancellation;
    private int minDurationDays;
    private double securityDeposit;
    private long availableFrom;
    private long[] unavailableDays;
    private double price;
    private int priceGroup;

    private boolean active;
    private boolean blocked;
    private boolean deleted;
    private long expirationDate;

    public AnnouncementArchive(long id, double latitude, double longitude, String country, String city, String street,
                               int building, PropertyType type, double totalSquare, int numBedroom, int numBathroom,
                               int floor, CharSequence preview, Set<Amenities> amenities, String ownerID, String title,
                               String text, int cancellation, int minDurationDays, double securityDeposit,
                               long availableFrom, long[] unavailableDays, double price, boolean active,
                               boolean blocked, boolean deleted, long expirationDate) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.type = type;
        this.totalSquare = totalSquare;
        this.numBedroom = numBedroom;
        this.numBathroom = numBathroom;
        this.floor = floor;
        this.preview = preview;
        this.amenities = amenities;
        this.ownerID = ownerID;
        this.title = title;
        this.text = text;
        this.cancellation = cancellation;
        this.minDurationDays = minDurationDays;
        this.securityDeposit = securityDeposit;
        this.availableFrom = availableFrom;
        this.unavailableDays = unavailableDays;
        this.price = price;
        this.priceGroup = 250*((int)(price/250));
        this.active = active;
        this.blocked = blocked;
        this.deleted = deleted;
        this.expirationDate = expirationDate;
    }

    public void setUnavaibleDays(Long[] unavaibleDays) {
        this.unavailableDays = unavailableDays;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setAvailableFrom(long availableFrom) {
        this.availableFrom = availableFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnouncementArchive that = (AnnouncementArchive) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}