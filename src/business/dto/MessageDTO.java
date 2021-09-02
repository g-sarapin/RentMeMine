package business.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    public String time;
    public String senderId;
    public String receiverId;
    public String senderMame;
    public String message;
    public Long announcementId;
    public Long messageBoxId;
}