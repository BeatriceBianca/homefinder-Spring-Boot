package epa.homefinder.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import epa.homefinder.dao.*;
import epa.homefinder.dto.*;
import epa.homefinder.entity.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private List<MultipartFile> uploadFiles;
    private UserDataDtoTransformer userDataDtoTransformer;
    private EmailService emailService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserDataDtoTransformer userDataDtoTransformer, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDataDtoTransformer = userDataDtoTransformer;
        this.emailService = emailService;
    }

    public User transformUserDto(UserRegistrationDto userRegistrationDto){
        User user = null;
        if(userRegistrationDto != null && userRepository.findByEmail(userRegistrationDto.getEmail()) == null) {
            user = new User();
            user.setUsername(userRegistrationDto.getUsername());
            user.setEmail(userRegistrationDto.getEmail());
            user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
            user.setPhone(userRegistrationDto.getPhoneNumber());
            user.setEnabled(true);
            String s = userRegistrationDto.getUserType();
            if(s.equals("Cumparator/Chirias"))
                user.setType(UserType.USER);
            else if(s.equals("Admin"))
                user.setType(UserType.ADMIN);
            else
                user.setType(UserType.AGENT_IMOBILIAR);
            user.setLastPasswordResetDate(this.getCurrentTime());
            user.setLastLoginDate(this.getCurrentTime());
            user.setCurrentLoginDate(this.getCurrentTime());
        }
        return user;
    }

    public void registerNewUser(UserRegistrationDto userRegistrationDto){
        userRepository.save(this.transformUserDto(userRegistrationDto));
        List<String> user = new ArrayList<>();
        user.add(userRegistrationDto.getEmail());
        try {
            emailService.sendEmail(user, "Bun venit pe HomeFinder!", "Bine ai venit pe HomeFinder, "
                                                                            + userRegistrationDto.getUsername() +"!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Timestamp getCurrentTime(){
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public UserDto getUserData (EmailDto emailDto) {
        UserDtoTransformer userDtoTransformer = new UserDtoTransformer();
        User user = userRepository.findByEmail(emailDto.getEmail());
        user.setLastLoginDate(user.getCurrentLoginDate());
        user.setCurrentLoginDate(new Date());
        userRepository.save(user);
        UserDto userDto = userDtoTransformer.transform(user);
        user.setNotification(null);
        userRepository.save(user);
        return userDto;
    }

    public void updateUserData(UserDtoUpdate userDtoUpdate) {
        User user = userRepository.findByEmail(userDtoUpdate.getMail());
        user.setPassword(passwordEncoder.encode(userDtoUpdate.getPassword()));
        user.setPhone(userDtoUpdate.getPhone());
        userRepository.save(user);
    }

    public List<String> getUserEmails() {
        return userRepository.getAllUsersEmail();
    }

    public List<UserDataDto> getUsersData() {
        return userDataDtoTransformer.transformList(userRepository.getAllUsers());
    }

    public void updateUser(UserDataDto userDataDto) {
        User user = userRepository.findUserByEmail(userDataDto.getEmail());
        user.setEnabled(userDataDto.getEnabled());
        userRepository.save(user);
    }
}
