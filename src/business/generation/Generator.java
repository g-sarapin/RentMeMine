package business.generation;

import business.documents.Amenities;
import business.documents.Announcement;
import business.documents.Person;
import business.documents.PropertyType;
import business.dto.Mapper;
import business.dto.PersonLessorFullDTO;
import business.services.account_service.IAccountService;
import business.services.admin_service.IAdminService;
import business.services.announcement_service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.security_logic.controllers.SecurityController;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class Generator implements IGenerator {

    static String[] countries = {"Israel", "Germany", "Spain", "France", "Polin", "Romania"};
    static String[] cities = {"Tel-aviv", "Haifa", "Kiev", "Berlin", "Paris", "Vienna", "Madrid", "Jerusalem", "Moscow", "Kyzly-Myrdy"};
    static String[] streets = {"Freedom", "Truth", "Heroes", "Independency", "Bank", "Horse", "Eagle", "Tree", "Flowers"};

    @Autowired
    IAnnouncementService annServ;

    @Autowired
    IAccountService accServ;

    @Autowired
    IAdminService admServ;

    @Autowired
    SecurityController secContr;

    @Override
    public void generatePerson(String login){

        String about = "My name is " + login + " and I'm the best";
        String eMail = login.toLowerCase() + "@gmail.com";
        String name = login;
        Map<String, String> phones = new HashMap<>();
        phones.put(login, "555-55-55");
        CharSequence photo = null;

        Person person = new Person(login, about, eMail, name, phones, photo);

        boolean lessor = false;

        if (true){// ThreadLocalRandom.current().nextBoolean()){
            lessor = true;
            int numAnn = ThreadLocalRandom.current().nextInt(40, 50);
            for (int i = 0 ; i < numAnn ; i++ ) {
                Announcement announcement = generateAnnouncement(login);
                annServ.saveAnnouncement(announcement);
                person.getAnnouncements().add(announcement.getId());
            }
        }

        PersonLessorFullDTO dto = Mapper.getPersonLessorFullDTO(person);
        dto.password = "12345";
        secContr.addUser(dto);
        if (lessor) admServ.setLessor(login);
    }

    @Override
    public Announcement generateAnnouncement(String ownerId){
        long id = annServ.getNewId();
        double latitude = ThreadLocalRandom.current().nextDouble(32,35);
        double longitude = ThreadLocalRandom.current().nextDouble(32,35);
        String country = countries[ThreadLocalRandom.current().nextInt(countries.length)];
        String city = cities[ThreadLocalRandom.current().nextInt(cities.length)];;
        String street = streets[ThreadLocalRandom.current().nextInt(streets.length)];
        int building = ThreadLocalRandom.current().nextInt(20);
        PropertyType type = PropertyType.values()[ThreadLocalRandom.current().nextInt(PropertyType.values().length)];
        double totalSquare = ThreadLocalRandom.current().nextDouble(20,150);
        int numBedroom = (int)totalSquare/20;
        int numBathroom = numBedroom/3;
        int floor = ThreadLocalRandom.current().nextInt(25);
        CharSequence preview = null;

        Set<Amenities> amenities = Arrays.stream(Amenities.values()).limit((int)Math.pow(ThreadLocalRandom.current().
                nextInt(Amenities.values().length*Amenities.values().length), .5)).collect(Collectors.toSet());

        String title = "";
        String text = "";
        int cancellation = 7*ThreadLocalRandom.current().nextInt(14);
        int durationUnit = 7*ThreadLocalRandom.current().nextInt(14);
        double securityDeposit = 100*ThreadLocalRandom.current().nextInt(20, 200);
        long availableFrom = LocalDate.now().toEpochDay() - 50 + ThreadLocalRandom.current().nextLong(400);
        double price = 100*ThreadLocalRandom.current().nextInt(20, 200);

        boolean active = true;
        boolean blocked = false;
        boolean deleted = false;
        long expirationDate = LocalDate.now().toEpochDay() - 50 + ThreadLocalRandom.current().nextLong(400);;

        return new Announcement(id, latitude, longitude, country, city, street, building, type, totalSquare,
                numBedroom, numBathroom, floor, preview, amenities, ownerId, title, text, cancellation, durationUnit,
                securityDeposit, availableFrom, new long[0], price, active, blocked, deleted, expirationDate);
    }

}