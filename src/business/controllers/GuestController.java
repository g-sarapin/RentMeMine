package business.controllers;

import business.dto.*;
import business.services.guest_service.IGuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.security_logic.jwt.JWTTokenUtil;
import java.util.List;
import static business.api.BUSINESS_API.*;

@RestController
@RequestMapping(GUEST)
@CrossOrigin
public class GuestController{

    @Autowired
    IGuestService service;

    @Autowired
    JWTTokenUtil tokenUtil;

    @GetMapping("/test")
    String testHeroku() {return "hi heroku?";}
    //*
    @GetMapping(GET_LESSOR_PROFILE)
    PersonLessorDTO getLessorProfile(@RequestParam String id){
        return service.getLessorProfile(id);
    }
    //*
    @GetMapping(GET_ANNOUNCEMENT)
    AnnouncementDTO getAnnouncement(@RequestParam long id){
        return service.getAnnouncement(id);
    }

    @GetMapping(GET_ANNOUNCEMENTS)
    List<AnnouncementDTO> getAnnouncements(@RequestParam List<Long> ids){return service.getAnnouncements(ids);}

    @GetMapping(GET_PICTURES)
    PicturesDTO getPhoto(@RequestParam long id){
        return service.getPhoto(id);
    }
    //*
    @PostMapping(GET_DROP_DOWN_SUGGEST)
    DropDownResponseDTO getQuickSearchInfo(@RequestBody DropDownRequestDTO quickSearch){
        return service.getDropDownSuggest(quickSearch);
    }
    //*
    @PostMapping(FIND_ANNOUNCEMENTS)
    List<AnnouncementDTO> findAnnouncement(@RequestBody FindRequestDTO findRequest){
        return service.findAnnouncement(findRequest);
    }
    //*
    @GetMapping(GET_DROP_DOWN_OPTIONS)
    DropDownResponseDTO getDropDownOptions(){
        return service.getDropDownOptions();
    }
    //*
    @PostMapping(SEND_SIMPLE_MESSAGE)
    String sendMessage(@RequestBody SimpleMessageDTO message){
        return service.sendSimpleMessage(message);
    }
    //*
    @PutMapping(ADD_EMAIL)
    String addEmail(@RequestParam String email){
        return service.addEmail(email);
    }

    @GetMapping("/getFake")
    String getFake(){
        return service.getFake();
    }
}