package business.services.messageBox_service;

import business.documents.Announcement;
import business.documents.Message;
import business.documents.MessageBox;
import business.documents.Person;
import business.dto.Mapper;
import business.dto.MessageBoxDTO;
import business.dto.MessageDTO;
import business.exceptions.DataBaseException;
import business.repositories.MessageBoxRepository;
import business.services.account_service.IAccountService;
import business.services.announcement_service.IAnnouncementService;
import business.services.email_service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageBoxService implements IMessageBoxService {

    @Autowired
    IAnnouncementService annServ;

    @Autowired
    IAccountService accServ;

    @Autowired
    MessageBoxRepository msgRepo;

    @Autowired
    IEmailService eMailServ;

    @Override
    @Transactional
    public MessageBoxDTO sendMessage(String secId, MessageDTO message) {
        Person sender = accServ.getPerson(message.senderId, secId, true, true);
        Person receiver = accServ.getPerson(message.receiverId, null, true, true);
        MessageBox msgBox = message.messageBoxId != null ? msgRepo.findById(message.messageBoxId)
                .orElseThrow(() -> new DataBaseException("Current conversation not found"))
                : createMessageBox(sender, receiver, message.announcementId);
        msgBox.getMessages().add(new Message(message.time, message.senderMame, message.message));
        receiver.getUnreadMessageBoxes().add(msgBox.getId());
        accServ.savePerson(receiver);
        msgBox = msgRepo.save(msgBox);
        return Mapper.getMessageBoxDTO(msgBox);
    }

    private MessageBox createMessageBox(Person sender, Person receiver, long announcementId){
        Announcement announcement = annServ.getAnnouncement(announcementId, null, true, true);
        MessageBox msgBox = new MessageBox(msgRepo.count() + 1, sender.getLogin(), receiver.getLogin(),announcementId,
                LocalDate.now().toEpochDay(), LocalDate.now().plusDays(announcement.getMinDurationDays()).toEpochDay(),
                announcement.getPrice(), 0, announcement.getCancellation(), announcement.getSecurityDeposit());
        sender.getMessageBoxIds().add(msgBox.getId());
        receiver.getMessageBoxIds().add(msgBox.getId());
        accServ.savePerson(sender);
        eMailServ.sendSimpleEmail(receiver.getEMail(), "You've got message from new client!", "Congratulations " +
                sender.getName() + " started a new conversation with you, regarding to your announcement '" +
                announcement.getTitle() + "'");
        return msgBox;
    }

    @Override
    @Transactional
    public MessageBoxDTO getMessageBox(String secId, long boxId) {
        MessageBox msgBox = msgRepo.findById(boxId)
                .orElseThrow(() -> new DataBaseException("Current MessageBox not found!"));
        if (secId != null) {
            if (!secId.equals(msgBox.getLessorId()) && !secId.equals(msgBox.getUserId()))
                throw new SecurityException("Your not an owner of this message box!");
            Person reader = accServ.getPerson(secId, null, false, false);
            if (reader.getUnreadMessageBoxes().remove(boxId)) accServ.savePerson(reader);
        }
        return Mapper.getMessageBoxDTO(msgBox);
    }

    @Override
    @Transactional
    public List<MessageBoxDTO> getMessageBoxes(String secId, List<Long> boxIds) {
        return ((List<MessageBox>) msgRepo.findAllById(boxIds)).stream()
                .peek(mb -> {if(!mb.getLessorId().equals(secId) && !mb.getUserId().equals(secId))
                    throw new SecurityException("Your not an owner of one of message boxes!");})
                .map(mb -> Mapper.getMessageBoxDTO(mb)).collect(Collectors.toList());
    }
}