package epa.homefinder.dto;

import lombok.*;
import epa.homefinder.entity.UserType;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDtoRequest {
    private Long idReview;
    private String comment;
    private Integer rating;
    private Integer like;
    private String username;
    private String mail;
    private UserType userType;
    private Date date;
}
