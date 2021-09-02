package business.documents;

import lombok.*;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
public class PriceCounter {

    @Id
    public int price;
    public int num;

    @Override
    public String toString() {
        return price + " : " + num + "\n";
    }
}