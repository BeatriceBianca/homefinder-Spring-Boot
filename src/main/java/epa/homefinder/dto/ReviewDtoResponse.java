package epa.homefinder.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDtoResponse {
    private Long idReview;
    private String comment;
    private Integer rating;
    private Integer like;
    private String mail;
    private Long adId;
}
