package epa.homefinder.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewAdDto {
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
    private String partitioning;
    private Integer comfort;
    private String furnished;
    private String floorLevel;
    private Double areaSurface;
    private Integer yearBuilt;
    private String location;
    private List<MultipartFile> uploadFiles;

    @Override
    public String toString() {
        return "NewAdDto{" +
                "title='" + title + '\'' +
                ", adType='" + adType + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", surface=" + surface +
                ", rooms=" + rooms +
                ", adItemType='" + adItemType + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
