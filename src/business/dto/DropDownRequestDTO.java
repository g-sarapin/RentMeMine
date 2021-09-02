package business.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public class DropDownRequestDTO {
    public String city;
    public Integer priceFrom;
    public Integer priceTo;
    public String[] type;
    public String dateFrom;
    public String dateTo;
    public String availableFrom;
    public Integer[] rooms;
    public String[] amenities;
}