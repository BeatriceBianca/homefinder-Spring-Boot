package epa.homefinder.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDto {
    private String username;
    private String email;
    private String password;
    private String userType;
//    private UserType userType;
    private Long phoneNumber;

    @Override
    public String toString() {
        return username +" "+" "+ email+" " +" "+  password +" "+  userType+" " + phoneNumber;
    }
}
