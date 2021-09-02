package business.services.search_service;

import business.documents.Announcement;
import business.dto.DropDownRequestDTO;
import business.dto.DropDownResponseDTO;
import business.dto.FindRequestDTO;

import java.util.List;

public interface ISearchService {
    DropDownResponseDTO getResponse(DropDownRequestDTO request);

    String[] getCities();

    List<Announcement> getAnnouncements(FindRequestDTO findRequest);
}