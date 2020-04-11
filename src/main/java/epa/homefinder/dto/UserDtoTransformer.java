package epa.homefinder.dto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import epa.homefinder.entity.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class UserDtoTransformer {

    public UserDto transform(User user){

        String userLastLoginDate = "Never";

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if(user.getLastLoginDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(user.getLastLoginDate());
            cal.add(Calendar.HOUR_OF_DAY, 3);
            userLastLoginDate = dateFormat.format(cal.getTime());
        }
        System.out.println(user.isEnabled());

        return UserDto.builder()
                .mail(user.getEmail())
                .name(user.getUsername())
                .userType(user.getType())
                .phone(user.getPhone())
                .lastLoginDate(userLastLoginDate)
                .enabled(user.isEnabled())
                .notification(user.getNotification())
                .build();
    }

    public List<UserDto> getUserList(List<User> user) {
        return user.stream().map(this::transform).collect(Collectors.toList());
    }
}
