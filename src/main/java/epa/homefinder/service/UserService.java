package epa.homefinder.service;

import org.apache.avro.generic.GenericData;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import epa.homefinder.dao.*;
import epa.homefinder.dto.*;
import epa.homefinder.entity.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private AdRepository adRepository;
    private PasswordEncoder passwordEncoder;
    private AdImageRepository adImageRepository;
    private List<MultipartFile> uploadFiles;
    private FavoriteRepository favoriteRepository;
    private ReviewRepository reviewRepository;
    private EventRepository eventRepository;
    private EventDtoTransformer eventDtoTransformer;
    private UserDataDtoTransformer userDataDtoTransformer;
    private EmailService emailService;

    public UserService(UserRepository userRepository, AdRepository adRepository,
                       PasswordEncoder passwordEncoder, AdImageRepository adImageRepository,
                       FavoriteRepository favoriteRepository, ReviewRepository reviewRepository,
                       EventRepository eventRepository, EventDtoTransformer eventDtoTransformer,
                       UserDataDtoTransformer userDataDtoTransformer, EmailService emailService) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.passwordEncoder = passwordEncoder;
        this.adImageRepository = adImageRepository;
        this.favoriteRepository = favoriteRepository;
        this.reviewRepository = reviewRepository;
        this.eventRepository = eventRepository;
        this.eventDtoTransformer = eventDtoTransformer;
        this.userDataDtoTransformer = userDataDtoTransformer;
        this.emailService = emailService;
    }

    public void setUploadFiles(List<MultipartFile> files) {
        uploadFiles = files;
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

    public void saveAdInfo(NewAdDto newAdDto) throws IOException {
        Ad newAd = new Ad();
        newAd.setTitle(newAdDto.getTitle());
        newAd.setDescription(newAdDto.getDescription());
        newAd.setAdTranzactionType(newAdDto.getAdType());
        newAd.setAdType(newAdDto.getAdItemType());
        newAd.setPrice(newAdDto.getPrice());
        newAd.setRoomNumber(newAdDto.getRooms());
        newAd.setSurface(newAdDto.getSurface());
        newAd.setUserId(userRepository.findByEmail(newAdDto.getUserEmail()));
        newAd.setLat(newAdDto.getLat());
        newAd.setLng(newAdDto.getLng());
        newAd.setDate(new Date());
        newAd.setSurface(newAdDto.getSurface());
        newAd.setAreaSurface(newAdDto.getAreaSurface());
        newAd.setFloorLevel(newAdDto.getFloorLevel());
        newAd.setComfort(newAdDto.getComfort());
        newAd.setPartitioning(newAdDto.getPartitioning());
        newAd.setFurnished(newAdDto.getFurnished());
        newAd.setYearBuilt(newAdDto.getYearBuilt());
        newAd.setLocation(newAdDto.getLocation());
//        if(newAdDto.getUploadFiles().size() > 0) {
//            this.saveImages(newAdDto.getUploadFiles(), newAd);
//        }
    }

    private void saveImages(List<MultipartFile> images, Ad adId) {
        for(MultipartFile file : images) {
            AdImage adImage = new AdImage();
            adImage.setAdId(adId);
            try {
                byte[] image = file.getBytes();
                adImage.setImage(image);
                adImageRepository.save(adImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void replaceImages(Long adId, List<MultipartFile> images) {
        Ad ad = adRepository.findById(adId).get();
        adImageRepository.deleteAllByAdId(ad.getAdId());
        this.saveImages(images, ad);
    }

    public void updateAdInfo(AdDto adDto) {
        Ad ad = adRepository.findById(adDto.getId()).get();
        ad.setTitle(adDto.getTitle());
        ad.setDescription(adDto.getDescription());
        ad.setAdTranzactionType(adDto.getAdType());
        ad.setAdType(adDto.getAdItemType());
        ad.setPrice(adDto.getPrice());
        ad.setRoomNumber(adDto.getRooms());
        ad.setSurface(adDto.getSurface());
        ad.setUserId(userRepository.findByEmail(adDto.getUserEmail()));
        ad.setLat(adDto.getLat());
        ad.setLng(adDto.getLng());
        ad.setSurface(adDto.getSurface());
        ad.setAreaSurface(adDto.getAreaSurface());
        ad.setFloorLevel(adDto.getFloorLevel());
        ad.setComfort(adDto.getComfort());
        ad.setPartitioning(adDto.getPartitioning());
        ad.setFurnished(adDto.getFurnished());
        ad.setYearBuilt(adDto.getYearBuilt());
        adRepository.save(ad);
    }

    public List<AdDto> getAllAdsWithFirstImage() {
        List<AdDto> adDtoList = new ArrayList<>();
        List<Ad> adList = adRepository.findAllByOrderByDateDesc();
        for(Ad i : adList) {
            System.out.println(i.getAdId());
            AdDto adDto = new AdDto();
            AdImage adImage = adImageRepository.findFirstByAdId(i);
            if(adImage != null) {
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
            adDto.setLocation(i.getLocation());
            adDtoList.add(adDto);
        }
        return adDtoList;
    }

    public AdDto adDtoBuilder(Ad i) {
        AdDto adDto = new AdDto();
        AdImage adImage = adImageRepository.findFirstByAdId(i);
        if(adImage != null) {
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

    public List<AdDto> getUserAds(EmailDto emailDto) {
        List<AdDto> adDtoList = new ArrayList<>();
        User currentUser = userRepository.findByEmail(emailDto.getEmail());
        List<Ad> adList = adRepository.findAllByUserIdOrderByDateDesc(currentUser);
        for(Ad i : adList) {
            adDtoList.add(this.adDtoBuilder(i));
        }
        return adDtoList;
    }

    public AdDetailsDto getAdInfo(Long adId) {
        DecimalFormat decimalFormat = new DecimalFormat(".##");
        AdDetailsDto adDetailsDto = new AdDetailsDto();
        Ad ad = adRepository.findById(adId).get();
        Double avgRating = reviewRepository.selectAvgReview(ad);
        System.out.println(avgRating);
        adDetailsDto.setId(ad.getAdId());
        adDetailsDto.setTitle(ad.getTitle());
        adDetailsDto.setDescription(ad.getDescription());
        adDetailsDto.setAdType(ad.getAdTranzactionType());
        adDetailsDto.setAdItemType(ad.getAdType());
        adDetailsDto.setPrice(ad.getPrice());
        adDetailsDto.setRooms(ad.getRoomNumber());
        adDetailsDto.setSurface(ad.getSurface());
        adDetailsDto.setLat(ad.getLat());
        adDetailsDto.setLng(ad.getLng());
        adDetailsDto.setPartitioning(ad.getPartitioning());
        adDetailsDto.setComfort(ad.getComfort());
        adDetailsDto.setFurnished(ad.getFurnished());
        adDetailsDto.setFloorLevel(ad.getFloorLevel());
        adDetailsDto.setAreaSurface(ad.getAreaSurface());
        adDetailsDto.setYearBuilt(ad.getYearBuilt());
        UserDto newUserDto = new UserDto();
        newUserDto.setName(ad.getUserId().getUsername());
        newUserDto.setMail(ad.getUserId().getEmail());
        newUserDto.setPhone(ad.getUserId().getPhone());
        newUserDto.setUserType(ad.getUserId().getType());
        newUserDto.setLastLoginDate(ad.getUserId().getLastLoginDate().toString());
        adDetailsDto.setUserDetails(newUserDto);
        if (avgRating != null) {
            String avgAdReview = decimalFormat.format(avgRating);
            adDetailsDto.setAvgAdReview(Double.parseDouble(avgAdReview));
        }
        return adDetailsDto;
    }

    public List<String> getAdImages(Long adId) {
        Ad ad = adRepository.findById(adId).get();
        List<AdImage> images = adImageRepository.findAllByAdId(ad);
        List<String> encodedImages = new ArrayList<>();
        for(AdImage adImage: images) {
            String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(adImage.getImage());
            encodedImages.add(encodeImage);
        }
        return encodedImages;
    }

    public void deleteAdById(Long adId) {
        Ad ad = adRepository.findById(adId).get();
        List<User> userList = eventRepository.findAllUsersAtAd(ad);
        List<String> userEmails = new ArrayList<>();
        for(User i : userList) {
            i.setNotification(2L);
            userRepository.save(i);
            userEmails.add(i.getEmail());
            try {
                emailService.sendEmail(userEmails,"Programare anulata"
                        , "Cu parere de rau, te informam ca programarea ta a fost anulata" +
                                " deaoarece anuntul " + ad.getTitle() + " a fost sters!");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        adRepository.deleteById(adId);

    }

    public List<AdDto> getAllFavorites(String userEmail) {
        List<AdDto> adDtoList = new ArrayList<>();
        User user = userRepository.findByEmail(userEmail);
        List<Ad> adList = new ArrayList<>();
        user.getFavorites().forEach(favorite -> {
            System.out.println(favorite.getAd().getAdId());
            adList.add(favorite.getAd());
        });
        adList.forEach(ad -> {
            adDtoList.add(this.adDtoBuilder(ad));
        });
        return adDtoList;
    }

    public void saveFavorite(FavoriteDto favoriteDto) {
        Favorite favorite = new Favorite();
        favorite.setAd(adRepository.findById(favoriteDto.getAdId()).get());
        favorite.setUser(userRepository.findByEmail(favoriteDto.getUserEmail()));
        favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Long favoriteAdId) {
//        favoriteRepository.deleteById(favoriteId);
        favoriteRepository.deleteByAdAdId(favoriteAdId);
    }

    public List<ReviewDtoRequest> getAdReviews(Long adId) {
        List<Review> reviewList = reviewRepository.findAllByAdOrderByIdDesc(adRepository.findById(adId).get());
        List<ReviewDtoRequest> reviewDtoRequestList = new ArrayList<>();
        for (Review review : reviewList) {
            ReviewDtoRequest reviewDtoRequest = ReviewDtoRequest.builder().idReview(review.getId())
                    .comment(review.getComment())
                    .rating(review.getRating())
                    .like(review.getRating())
                    .username(review.getUser().getUsername())
                    .userType(review.getUser().getType())
                    .mail(review.getUser().getEmail())
                    .date(review.getDate())
                    .build();
            reviewDtoRequestList.add(reviewDtoRequest);
        }
        return  reviewDtoRequestList;
    }

    public Long saveAdReview(ReviewDtoResponse reviewDtoResponse) {
        Review review = Review.builder().ad(adRepository.findById(reviewDtoResponse.getAdId()).get())
                                        .user(userRepository.findByEmail(reviewDtoResponse.getMail()))
                                        .comment(reviewDtoResponse.getComment())
                                        .rating(reviewDtoResponse.getRating())
                                        .like(reviewDtoResponse.getLike())
                                        .date(this.getCurrentTime())
                                        .build();
        Review review1 = reviewRepository.save(review);
        return review1.getId();
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.delete(reviewRepository.findById(reviewId).get());
    }

    public void updateReview(ReviewDtoResponse reviewDtoResponse) {
        Review review = reviewRepository.findById(reviewDtoResponse.getIdReview()).get();
        review.setComment(reviewDtoResponse.getComment());
        review.setRating(reviewDtoResponse.getRating());
        review.setLike(reviewDtoResponse.getLike());
        reviewRepository.save(review);
    }

    public List<String> getUserEmails() {
        return userRepository.getAllUsersEmail();
    }

    public void saveEvent(EventDtoDate eventDto) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        Event event = new Event();
        Ad ad = adRepository.findById(eventDto.getAdId()).get();
        event.setAd(ad);
        event.setUser(userRepository.findByEmail(eventDto.getUserEmail()));
        event.setStatus(eventDto.getStatus());
        event.setMessage(eventDto.getMessage());
        event.setStartDate(simpleDateFormat.parse(eventDto.getStartDate()));
        event.setEndDate(simpleDateFormat.parse(eventDto.getEndDate()));
        eventRepository.save(event);
        User user = eventRepository.findOwner(adRepository.findById(eventDto.getAdId()).get());
        user.setNotification(3L);
        userRepository.save(user);
        List<String> receivers = new ArrayList<>();
        receivers.add(eventDto.getUserEmail());
        try {
            emailService.sendEmail(receivers,"Programare vizita" , "Programarea vizitei la " + ad.getTitle()
                    + " a fost inregistrata cu succes!");
            receivers.remove(0);
            receivers.add(user.getEmail());
            emailService.sendEmail(receivers, "Programare noua", "Buna " + user.getEmail() + "!"
                    + "\n" + "O noua programare la " + ad.getTitle() + " asteapta confirmarea ta!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public List<EventDto> getUserEvents(EmailDto emailDto) {
        User user = userRepository.findByEmail(emailDto.getEmail());
        List<EventDto> eventDtos = eventDtoTransformer.transformList(user.getEvents(), false);
        if (eventDtos.size() == 0) {
            eventDtos = eventDtoTransformer.transformList(eventRepository.findAllByAdUserId(user), true);
        }
        return eventDtos;
    }

    public List<EventDto> getAdEvents(Long adId) {
        return eventDtoTransformer.transformList(adRepository.findById(adId).get().getEvents(), false);
    }

    public void deleteEvent(EventDtoDate eventDto) {
        Ad ad = adRepository.findById(eventDto.getAdId()).get();
        Event event = eventRepository.findByUserAndAd(
                userRepository.findByEmail(eventDto.getUserEmail()),ad);
        eventRepository.deleteByUserAndAd(
                userRepository.findByEmail(eventDto.getUserEmail()),ad);
        User user = userRepository.findUserByEmail(eventDto.getUserEmail());
        user.setNotification(2L);
        userRepository.save(user);
        List<String> users = new ArrayList<>();
        users.add(user.getEmail());
        try {
            emailService.sendEmail(users, "Update programare " + ad.getTitle(),
                    "Buna " + event.getUser().getUsername() + "!" + "\n" + "Cu parere de rau te informam ca programarea ta a fost refuzata.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void updateEvent(EventDtoDate eventDto) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        Ad ad = adRepository.findById(eventDto.getAdId()).get();
        Event event = eventRepository.findByUserAndAd(
                userRepository.findByEmail(eventDto.getUserEmail()),ad);
        event.setStatus(eventDto.getStatus());
        event.setMessage(eventDto.getMessage());
        event.setStartDate(simpleDateFormat.parse(eventDto.getStartDate()));
        event.setEndDate(simpleDateFormat.parse(eventDto.getEndDate()));
        User user = userRepository.findUserByEmail(eventDto.getUserEmail());
        user.setNotification(1L);
        userRepository.save(user);
        eventRepository.save(event);
        List<String> users = new ArrayList<>();
        users.add(user.getEmail());
        try {
            emailService.sendEmail(users, "Update programare " + ad.getTitle(),
                    "Buna " + event.getUser().getUsername() + "!" + "\n" + "Dorim sa te informam ca programarea ta a fost acceptata.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public List<UserDataDto> getUsersData() {
        return userDataDtoTransformer.transformList(userRepository.getAllUsers());
    }

    public void updateUser(UserDataDto userDataDto) {
        User user = userRepository.findUserByEmail(userDataDto.getEmail());
        user.setEnabled(userDataDto.getEnabled());
        userRepository.save(user);
    }

    public ReportsDto getEventsReport() {
        Long adsValue = adRepository.count();
        return ReportsDto.builder()
                .acceptedEvents(eventRepository.getAcceptedEventsNumber())
                .pendingEvents(eventRepository.getPendingEventsNumber())
                .allEvents(eventRepository.getEventsTotal())
                .allAds(adsValue.intValue())
                .apartmentAds(adRepository.getApartmentAdsNumber())
                .homeAds(adRepository.getHomeAdsNumber())
                .rentAds(adRepository.getRentAdsNumber())
                .salesAds(adRepository.getSalesAdsNumber())
                .build();
    }
}
