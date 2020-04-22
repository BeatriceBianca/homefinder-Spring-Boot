package epa.homefinder.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdDto {
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
    private String userEmail;
    private String image;
//    private byte[] image;
    private String partitioning;
    private Integer comfort;
    private String furnished;
    private String floorLevel;
    private Double areaSurface;
    private Integer yearBuilt;
    private String location;
}
