package business.dto;

import business.documents.Message;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class MessageBoxDTO {

    public long id;
    public String userId;
    public String lessorId;
    public Long announcementId;
    public List<Message> messages;
    // Deal Data
    public String begin;
    public String end;
    public double price;
    public int discount;
    public int cancellation;
    public double securityDeposit;
}