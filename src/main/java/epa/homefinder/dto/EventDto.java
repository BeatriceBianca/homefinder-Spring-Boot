package epa.homefinder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EventDto {
    private Long eventId;
    private String userEmail;
    private Long adId;
    private String adTitle;
    private String message;
    private String startDate;
    private String endDate;
    private String status;
    private Boolean owner;
}
