package business.dto;

import lombok.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class PicturesDTO {

    public long id;
    public String ownerId;
    public CharSequence[] pictures;

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PicturesDTO that = (PicturesDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}