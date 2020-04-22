package epa.homefinder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EventDtoDate {
    private Long eventId;
    private String userEmail;
    private Long adId;
    private String message;
//    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(timezone = "Europe/Bucharest")
//    private Date startDate;
    private String startDate;
//    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(timezone = "Europe/Bucharest")
//    private Date endDate;
    private String endDate;
    private String status;
}
