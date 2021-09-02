package business.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document(collection = "pictures")
public class Pictures {

    @Id
    private long id;
    private String ownerId;
    private CharSequence[] pictures;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pictures pictures = (Pictures) o;
        return id == pictures.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}