package business.services.messageBox_service;

import business.dto.MessageBoxDTO;
import business.dto.MessageDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMessageBoxService {
    @Transactional
    MessageBoxDTO sendMessage(String secId, MessageDTO message);

    @Transactional
    MessageBoxDTO getMessageBox(String secId, long boxId);

    @Transactional
    List<MessageBoxDTO> getMessageBoxes(String secId, List<Long> boxIds);
}