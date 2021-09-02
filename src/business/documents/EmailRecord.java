package business.documents;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Document(collection = "eMails")
public class EmailRecord {

    @Id
    public String eMail;
}