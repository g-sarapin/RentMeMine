package business.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="messageBoxes")
@NoArgsConstructor
@Getter
public class MessageBox {

    @Id
    private long id;
    private String userId;
    private String lessorId;
    private Long announcementId;
    private List<Message> messages = new ArrayList<>();
    // Deal Data
    private Long begin;
    private Long end;
    private double price;
    private int discount;
    private int cancellation;
    private double securityDeposit;

    public MessageBox(long id, String userId, String lessorId, Long announcementId, Long begin, Long end,
                      double price, int discount, int cancellation, double securityDeposit) {
        this.id = id;
        this.userId = userId;
        this.lessorId = lessorId;
        this.announcementId = announcementId;
        this.begin = begin;
        this.end = end;
        this.price = price;
        this.discount = discount;
        this.cancellation = cancellation;
        this.securityDeposit = securityDeposit;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}