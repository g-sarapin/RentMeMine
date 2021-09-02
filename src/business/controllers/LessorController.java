package business.controllers;

import business.dto.AnnouncementDTO;
import business.dto.PersonLessorFullDTO;
import business.dto.PicturesDTO;
import business.services.lessor_service.ILessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.security_logic.jwt.JWTTokenUtil;

import static business.api.BUSINESS_API.*;

@RestController
@RequestMapping(LESSOR)
@CrossOrigin
public class LessorController extends UserController{

    @Autowired
    ILessorService service;
    @Autowired
    JWTTokenUtil tokenUtil;

    //*
    @PostMapping(SET_ANNOUNCEMENT)
    AnnouncementDTO setAnnouncement(@RequestHeader("Authorization") String authHeader, @RequestBody AnnouncementDTO announcement){
        return service.setAnnouncement(tokenUtil.getUsernameFromToken(authHeader.substring(7)), announcement);
    }

    @PostMapping(SET_PICTURES)
    PicturesDTO setPictures(@RequestHeader("Authorization") String authHeader, @RequestBody PicturesDTO pictures){
        return service.setPictures(tokenUtil.getUsernameFromToken(authHeader.substring(7)), pictures);
    }
    //*
    @PutMapping(SET_ANNOUNCEMENT_ACTIVE)
    AnnouncementDTO setAnnouncementActive(@RequestHeader("Authorization") String authHeader, @RequestParam int id, @RequestParam boolean active){
        return service.setAnnouncementActive(tokenUtil.getUsernameFromToken(authHeader.substring(7)), id, active);
    }
    //*
    @PutMapping(MODIFY_ANNOUNCEMENT_EXPIRATION_DATE)
    AnnouncementDTO modifyExpirationDate(@RequestHeader("Authorization") String authHeader, @RequestParam int id, @RequestParam String date){
        return service.modifyExpirationDate(tokenUtil.getUsernameFromToken(authHeader.substring(7)), id, date);
    }
    //*
    @DeleteMapping(REMOVE_ANNOUNCEMENT)
    PersonLessorFullDTO removeAnnouncement(@RequestHeader("Authorization") String authHeader, @RequestParam int id){
        return super.service.removeAnnouncement(tokenUtil.getUsernameFromToken(authHeader.substring(7)), id);
    }

}