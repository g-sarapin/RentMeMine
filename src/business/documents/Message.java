package business.documents;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Message {
    private String time;
    private String sender;
    private String message;
}