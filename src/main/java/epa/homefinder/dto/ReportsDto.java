package epa.homefinder.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReportsDto {
    private Integer pendingEvents;
    private Integer acceptedEvents;
    private Integer allEvents;
    private Integer homeAds;
    private Integer apartmentAds;
    private Integer salesAds;
    private Integer rentAds;
    private Integer allAds;
}
