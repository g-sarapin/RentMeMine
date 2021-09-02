package business.services.guest_service;

import business.dto.*;

import java.util.List;

public interface IGuestService {

    DropDownResponseDTO getDropDownSuggest(DropDownRequestDTO quickSearch);

    List<AnnouncementDTO> findAnnouncement (FindRequestDTO findRequest);

    PersonLessorDTO getLessorProfile(String id);

    AnnouncementDTO getAnnouncement(long id);

    PicturesDTO getPhoto(long id);

    String sendSimpleMessage(SimpleMessageDTO simpleMessage);

    String addEmail(String eMail);

    DropDownResponseDTO getDropDownOptions();

    String getFake();

    List<AnnouncementDTO> getAnnouncements(List<Long> ids);
}