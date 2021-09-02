package business.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class FindRequestDTO {
    public String city;
    public Integer priceFrom;
    public Integer priceTo;
    public String[] type;
    public String dateFrom;
    public String dateTo;
    public String availableFrom;
    public Integer[] rooms;
    public String[] amenities;

    public Integer from;
    public Integer to;
    public String sortedBy;
}