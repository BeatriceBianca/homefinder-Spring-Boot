package epa.homefinder.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoUpdate {
    private String mail;
    private String password;
    private Long phone;
}
