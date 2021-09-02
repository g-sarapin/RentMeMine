package business.services.guest_service;

import business.documents.Amenities;
import business.documents.Announcement;
import business.documents.EmailRecord;
import business.documents.PropertyType;
import business.dto.*;
import business.exceptions.DataBaseException;
import business.generation.IGenerator;
import business.repositories.EmailRepository;
import business.repositories.PicturesRepository;
import business.services.account_service.IAccountService;
import business.services.announcement_service.IAnnouncementService;
import business.services.email_service.IEmailService;
import business.services.search_service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class GuestService implements IGuestService {

    @Autowired
    IAccountService accServ;

    @Autowired
    IAnnouncementService annServ;

    @Autowired
    PicturesRepository picRepo;

    @Autowired
    ISearchService findServ;

    @Autowired
    IEmailService mailServ;

    @Autowired
    EmailRepository eMailRepo;

    @Autowired
    ISearchService searchServ;

    @Autowired
    IGenerator gen;

    @Override
    public DropDownResponseDTO getDropDownSuggest(DropDownRequestDTO quickSearch){
        return findServ.getResponse(quickSearch);
    }

    @Override
    public List<AnnouncementDTO> findAnnouncement(FindRequestDTO findRequest){
        boolean end = false;
        findRequest.from = findRequest.from == null ? 0 : findRequest.from;
        findRequest.to = findRequest.to == null ? 10 : findRequest.to;
        int quantity = findRequest.to - findRequest.from;
        findRequest.to = findRequest.to + (int)1.25*(quantity);
        List<Announcement> rawList;
        List<AnnouncementDTO> res = new ArrayList<>();
        do{
            rawList = searchServ.getAnnouncements(findRequest);
            res.clear();
            if (rawList.size() < quantity) end = true;
            for (int i = 0 ; i < rawList.size() ; i++)
                if(annServ.checkAnnouncementDates(rawList.get(i)).isActive())
                    res.add(Mapper.getAnnouncementDTO(rawList.get(i)));
        } while(quantity > res.size() && !end);
        return end ? res : res.subList(0, quantity);
    }

    @Override
    public PersonLessorDTO getLessorProfile(String id) {
        return Mapper.getPersonLessorDTO(accServ.getPerson(id, null, true, false));
    }

    @Override
    public AnnouncementDTO getAnnouncement(long id) {
        return Mapper.getAnnouncementDTO(annServ.getAnnouncement(id, null, false,false));
    }

    @Override
    public PicturesDTO getPhoto(long id){
        annServ.getAnnouncement(id, null, true, true);
        return Mapper.getPicturesDTO(picRepo.findById(id).orElseThrow(() -> new DataBaseException("Photos not found!")));
    }

    @Override
    public String sendSimpleMessage(SimpleMessageDTO message){
        String eMail = accServ.getPerson(message.receiver, null, true, false).getEMail();
        mailServ.sendSimpleEmail(eMail, "RentMe. New message!",
                "From: " + message.name + "\nPhone: " + message.phone
                        + "\nE-Mail: " + message.eMail + "\n" + message.message);
        return "Message sent!";
    }

    public String addEmail(String eMail){ eMailRepo.save(new EmailRecord(eMail)); return "E-Mail saved!";}

    @Override
    public String getFake() {
        String[] owners = //getNameList();
                { "Misha", "Pasha", "Sasha", "Masha", "Glasha", "Dasha", "Yana", "Anna", "Katya", "Andrew", "Gleb", "Sergey",
                        "Anton", "Akasia", "Tanya", "Marmon", "Petya"};
        for (int i = 0 ; i < owners.length ; i++ ) gen.generatePerson(owners[i]);
        return "Ok";
    }

    @Override
    public List<AnnouncementDTO> getAnnouncements(List<Long> ids) {
        return annServ.getAnnouncements(ids).stream().map(ann -> Mapper.getAnnouncementDTO(ann)).collect(Collectors.toList());
    }

    private String[] getNameList() {
        ArrayList<String> res = new ArrayList<>();
        String[] literals = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String name;
        while(res.size() < 400){
            name = "";
            int num = ThreadLocalRandom.current().nextInt(3, 10);
            for (int i = 0; i < num ; i++){
                name += i == 0 ? literals[ThreadLocalRandom.current().nextInt(literals.length)].toUpperCase(Locale.ROOT) : literals[ThreadLocalRandom.current().nextInt(literals.length)];
            }
            res.add(name);
        }
        return res.toArray(new String[0]);
    }

    public DropDownResponseDTO getDropDownOptions(){
        return new DropDownResponseDTO(searchServ.getCities(), null,
                Arrays.stream(PropertyType.values()).map(PropertyType::toString).toArray(String[]::new),
                null , Arrays.stream(Amenities.values()).map(Amenities::toString).toArray(String[]::new), 0);
    }

}