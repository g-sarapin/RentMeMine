package business.controllers;

import business.dto.*;
import business.services.admin_service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.security_logic.jwt.JWTTokenUtil;

import static business.api.BUSINESS_API.*;

@RestController
@RequestMapping(ADMIN)
@CrossOrigin
public class AdminController extends LessorController {

    @Autowired
    IAdminService service;

    @Autowired
    JWTTokenUtil tokenUtil;
    //*
    @PutMapping(BLOCK_PERSON)
    String removePerson(@RequestParam String id){
        return service.setPersonBlocked(id, true);
    }
    //*
    @PutMapping(UNBLOCK_PERSON)
    String restorePerson(@RequestParam String id){
        return service.setPersonBlocked(id, false);
    }
    //*
    @PutMapping(BLOCK_ANNOUNCEMENT)
    String removeAnnouncement(@RequestParam long id){
        return service.setAnnouncementBlocked(id, true);
    }
    //*
    @PutMapping(UNBLOCK_ANNOUNCEMENT)
    String restoreAnnouncement(@RequestParam long id){
        return service.setAnnouncementBlocked(id, false);
    }
    //*
    @GetMapping(GET_FOREIGN_PROFILE)
    PersonLessorFullDTO getLessorFullProfile(@RequestParam String id){
        return (PersonLessorFullDTO) service.getFullProfile(id);
    }
    //*
    @PutMapping(SET_LESSOR)
    String setLessor(@RequestParam String id){
        return service.setLessor(id);
    }

    @GetMapping(GET_FOREIGN_MESSAGE_BOX)
    MessageBoxDTO getMessageBox(@RequestParam long id){
        return service.getMessageBox(id);
    }
    //*
    @PostMapping(SEND_SPAM)
    String sendSpam(@RequestBody Spam spam){
        return service.sendSpam(spam);
    }
}