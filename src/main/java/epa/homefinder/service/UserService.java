package epa.homefinder.service;

import epa.homefinder.dao.*;
import epa.homefinder.dto.*;
import epa.homefinder.entity.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventDtoTransformer eventDtoTransformer;
    private final UserDataDtoTransformer userDataDtoTransformer;
    private final EmailService emailService;
    private final AdImageRepository adImageRepository;

    public UserService(AdRepository adRepository, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, EventDtoTransformer eventDtoTransformer,
                       UserDataDtoTransformer userDataDtoTransformer, EmailService emailService, AdImageRepository adImageRepository) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventDtoTransformer = eventDtoTransformer;
        this.userDataDtoTransformer = userDataDtoTransformer;
        this.emailService = emailService;
        this.adImageRepository = adImageRepository;
    }

    public User transformUserDto(UserRegistrationDto userRegistrationDto) {
        User user = null;
        if (userRegistrationDto != null && userRepository.findByEmail(userRegistrationDto.getEmail()) == null) {
            user = new User();
            user.setUsername(userRegistrationDto.getUsername());
            user.setEmail(userRegistrationDto.getEmail());
            user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
            user.setPhone(userRegistrationDto.getPhoneNumber());
            user.setEnabled(true);
            String s = userRegistrationDto.getUserType();
            if (s.equals("Cumparator/Chirias"))
                user.setType(UserType.USER);
            else if (s.equals("Admin"))
                user.setType(UserType.ADMIN);
            else
                user.setType(UserType.AGENT_IMOBILIAR);
            user.setLastPasswordResetDate(this.getCurrentTime());
            user.setLastLoginDate(this.getCurrentTime());
            user.setCurrentLoginDate(this.getCurrentTime());
        }
        return user;
    }

    public void registerNewUser(UserRegistrationDto userRegistrationDto) {
        userRepository.save(this.transformUserDto(userRegistrationDto));
        List<String> user = new ArrayList<>();
        user.add(userRegistrationDto.getEmail());
        try {
            emailService.sendEmail(user, "Bun venit pe HomeFinder!", "Bine ai venit pe HomeFinder, "
                    + userRegistrationDto.getUsername() + "!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Timestamp getCurrentTime() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public UserDto getUserData(EmailDto emailDto) {
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

    public List<AdDto> getUserAds(EmailDto emailDto) {
        List<AdDto> adDtoList = new ArrayList<>();
        User currentUser = userRepository.findByEmail(emailDto.getEmail());
        List<Ad> adList = adRepository.findAllByUserIdOrderByDateDesc(currentUser);
        for (Ad i : adList) {
            adDtoList.add(this.adDtoBuilder(i));
        }
        return adDtoList;
    }

    public AdDto adDtoBuilder(Ad i) {
        AdDto adDto = new AdDto();
        AdImage adImage = adImageRepository.findFirstByAdId(i);
        if (adImage != null) {
            String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(adImage.getImage());
            adDto.setImage(encodeImage);
        }
        adDto.setId(i.getAdId());
        adDto.setTitle(i.getTitle());
        adDto.setAdType(i.getAdTranzactionType());
        adDto.setAdItemType(i.getAdType());
        adDto.setDescription(i.getDescription());
        adDto.setLat(i.getLat());
        adDto.setLng(i.getLng());
        adDto.setPrice(i.getPrice());
        adDto.setSurface(i.getSurface());
        adDto.setRooms(i.getRoomNumber());
        adDto.setUserEmail(i.getUserId().getEmail());
        adDto.setPartitioning(i.getPartitioning());
        adDto.setComfort(i.getComfort());
        adDto.setFurnished(i.getFurnished());
        adDto.setFloorLevel(i.getFloorLevel());
        adDto.setAreaSurface(i.getAreaSurface());
        adDto.setYearBuilt(i.getYearBuilt());
        return adDto;
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
