package business.dto;

import business.documents.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Mapper {

    public static PersonUserFullDTO getPersonUserFullDTO(Person person) {
        return new PersonUserFullDTO(person.getLogin(), person.getEMail(),
                person.getName(), person.getPhoto(), person.isBlocked(),
                person.getPhones(), person.getFavorites(), new ArrayList(person.getMessageBoxIds()),
                new ArrayList(person.getUnreadMessageBoxes()));
    }

    public static PersonLessorDTO getPersonLessorDTO(Person person) {
        return new PersonLessorDTO(person.getLogin(),person.getAbout(),person.getName(),
                person.getPhoto(), person.getAnnouncements());
    }

    public static PersonLessorFullDTO getPersonLessorFullDTO(Person person) {
        return new PersonLessorFullDTO(person.getLogin(), person.getEMail(), person.getName(), person.getPhoto(),
                person.isBlocked(), person.getPhones(), person.getFavorites(), new ArrayList(person.getMessageBoxIds()),
                new ArrayList(person.getUnreadMessageBoxes()), person.getAbout(), person.getAnnouncements());
    }

    public static AnnouncementDTO getAnnouncementDTO(Announcement announcement) {
        return new AnnouncementDTO(announcement.getId(), announcement.getLatitude(), announcement.getLongitude(),
                announcement.getCountry(), announcement.getCity(), announcement.getStreet(), announcement.getBuilding(),
                announcement.getType().toString(), announcement.getTotalSquare(), announcement.getNumBedroom(),
                announcement.getNumBathroom(), announcement.getFloor(), announcement.getPreview(),
                announcement.getAmenities().stream().map(Enum::name).toArray(String[]::new),
                announcement.getOwnerID(), announcement.getTitle(), announcement.getText(),
                announcement.getCancellation(), announcement.getMinDurationDays(), announcement.getSecurityDeposit(),
                LocalDate.ofEpochDay(announcement.getAvailableFrom()).toString(), announcement.getUnavailableDays(),
                announcement.getPrice(), announcement.isActive(), announcement.isBlocked(), announcement.isDeleted(),
                LocalDate.ofEpochDay(announcement.getExpirationDate()).toString());
    }

    public static PicturesDTO getPicturesDTO(Pictures photos) {
        return new PicturesDTO(photos.getId(), photos.getOwnerId(), photos.getPictures());
    }

    public static Pictures getPictures(PicturesDTO photos) {
        return new Pictures(photos.id, photos.ownerId, photos.pictures);
    }

    public static Announcement getAnnouncement(AnnouncementDTO announcement) {
        return new Announcement(announcement.id, announcement.latitude, announcement.longitude,
                announcement.country, announcement.city, announcement.street, announcement.building,
                PropertyType.valueOf(announcement.type), announcement.totalSquare, announcement.numBedroom,
                announcement.numBathroom, announcement.floor, announcement.preview,
                Arrays.stream(announcement.amenities).map(a -> Amenities.valueOf(a)).collect(Collectors.toSet()),
                announcement.ownerID, announcement.title, announcement.text, announcement.cancellation,
                announcement.minDurationDays, announcement.securityDeposit,
                announcement.availableFrom != null ? LocalDate.parse(announcement.availableFrom).toEpochDay() : LocalDate.now().toEpochDay(),
                announcement.unavailableDays, announcement.price, announcement.active, announcement.blocked, announcement.deleted,
                announcement.expirationDate != null ? LocalDate.parse(announcement.expirationDate).toEpochDay() : LocalDate.now().toEpochDay() + 365);
    }

    public static MessageBoxDTO getMessageBoxDTO(MessageBox messageBox) {
        return new MessageBoxDTO(messageBox.getId(), messageBox.getUserId(), messageBox.getLessorId(),
                messageBox.getAnnouncementId(), messageBox.getMessages(), messageBox.getBegin().toString(),
                messageBox.getEnd().toString(), messageBox.getPrice(), messageBox.getDiscount(),
                messageBox.getCancellation(), messageBox.getSecurityDeposit());
    }
}