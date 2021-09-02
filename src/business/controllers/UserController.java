package business.controllers;

import business.dto.MessageBoxDTO;
import business.dto.MessageDTO;
import business.dto.PersonLessorFullDTO;
import business.dto.PersonUserFullDTO;
import business.services.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.security_logic.jwt.JWTTokenUtil;

import java.util.List;

import static business.api.BUSINESS_API.*;


@RestController
@RequestMapping(USER)
@CrossOrigin
public class UserController extends  GuestController{


    @Autowired
    IUserService service;

    @Autowired
    JWTTokenUtil tokenUtil;
    //*
    @GetMapping(GET_FULL_PROFILE)
    PersonUserFullDTO getFullProfile(@RequestHeader("Authorization") String authHeader, @RequestParam String id){
        return service.getOwnProfile(tokenUtil.getUsernameFromToken(authHeader.substring(7)), id);
    }
    //*
    @PostMapping(SET_FULL_PROFILE)
    PersonUserFullDTO setUserFullProfile(@RequestHeader("Authorization") String authHeader, @RequestBody PersonLessorFullDTO profile){
        return service.setProfile(tokenUtil.getUsernameFromToken(authHeader.substring(7)),profile);
    }
    //*
    @DeleteMapping(REMOVE_ACCOUNT)
    String removePerson(@RequestHeader("Authorization") String authHeader, @RequestParam String id){
        return service.removeAccount(tokenUtil.getUsernameFromToken(authHeader.substring(7)), id, authHeader.substring(7));
    }
    //*
    @PutMapping(ADD_TO_FAVORITES)
    PersonUserFullDTO addToFavorites(@RequestHeader("Authorization") String authHeader, @RequestParam long id){
        return service.modifyFavorites(tokenUtil.getUsernameFromToken(authHeader.substring(7)), true, id);
    }
    //*
    @PutMapping(REMOVE_FROM_FAVORITES)
    PersonUserFullDTO removeFromFavorites(@RequestHeader("Authorization") String authHeader, @RequestParam long id){
        return service.modifyFavorites(tokenUtil.getUsernameFromToken(authHeader.substring(7)), false, id);
    }
    //*
    @PostMapping(SEND_MESSAGE)
    MessageBoxDTO sendMessage(@RequestHeader("Authorization") String authHeader, @RequestBody MessageDTO message){
        return service.sendMessage(tokenUtil.getUsernameFromToken(authHeader.substring(7)), message);
    }
    //*
    @GetMapping(GET_MESSAGE_BOX)
    MessageBoxDTO getMessageBox(@RequestHeader("Authorization") String authHeader, @RequestParam long id){
        return service.getMessageBox(tokenUtil.getUsernameFromToken(authHeader.substring(7)), id);
    }

    @GetMapping(GET_MESSAGE_BOXES)
    List<MessageBoxDTO> getMessageBoxes(@RequestHeader("Authorization") String authHeader, @RequestParam List<Long> ids){
        return service.getMessageBoxes(tokenUtil.getUsernameFromToken(authHeader.substring(7)), ids);
    }

}