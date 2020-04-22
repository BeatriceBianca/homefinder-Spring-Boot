package epa.homefinder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import epa.homefinder.entity.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdDetailsDto {
    private Long id;
    private String title;
    private String adType;
    private String description;
    private Double price;
    private Double surface;
    private Integer rooms;
    private String adItemType;
    private Double lat;
    private Double lng;
    private UserDto userDetails;
    private String partitioning;
    private Integer comfort;
    private String furnished;
    private String floorLevel;
    private Double areaSurface;
    private Integer yearBuilt;
    private Double avgAdReview;
}
