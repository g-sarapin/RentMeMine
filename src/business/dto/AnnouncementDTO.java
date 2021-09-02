package business.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {

    public Long id;
    public double latitude;
    public double longitude;
    public String country;
    public String city;
    public String street;
    public int building;
    public String type;
    public double totalSquare;
    public int numBedroom;
    public int numBathroom;
    public int floor;
    public CharSequence preview;

    public String[] amenities;

    public String ownerID;
    public String title;
    public String text;
    public int cancellation;
    public int minDurationDays;
    public double securityDeposit;
    public String availableFrom;
    public long[] unavailableDays;
    public double price;

    public boolean active;
    public boolean blocked;
    public boolean deleted;
    public String expirationDate;
}