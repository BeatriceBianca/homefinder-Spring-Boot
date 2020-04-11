package epa.homefinder.dto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import epa.homefinder.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class UserDataDtoTransformer {
    public UserDataDto transform(User user) {
        return UserDataDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .type(user.getType())
                .lastLoginDate(user.getLastLoginDate().toString())
                .lastPasswordResetDate(user.getLastPasswordResetDate().toString())
                .enabled(user.isEnabled())
                .build();
    }

    public List<UserDataDto> transformList(List<User> usersList) {
        return usersList.stream().map(this::transform).collect(Collectors.toList());
    }
}
