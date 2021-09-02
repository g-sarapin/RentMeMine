package business.dto;

import lombok.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
public class DropDownResponseDTO {
    public String[] city;
    public Map<Integer, Integer> prices;
    public String[] type;
    public int[] rooms;
    public String[] amenities;
    public Integer quantity;

    @Override
    public String toString() {
        return "QuickSearchResponseDTO{" +
                "city=" + Arrays.toString(city) +
                ", prices=" + prices +
                ", type=" + Arrays.toString(type) +
                ", rooms=" + Arrays.toString(rooms) +
                ", amenities=" + Arrays.toString(amenities) +
                ", quantity=" + quantity +
                '}';
    }
}