package epa.homefinder.service;

import epa.homefinder.dao.*;
import epa.homefinder.dto.*;
import epa.homefinder.entity.Ad;
import epa.homefinder.entity.AdImage;
import epa.homefinder.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AdService {
    private final AdRepository adRepository;
    private final AdImageRepository adImageRepository;
    private final UserRepository userRepository;
    private List<MultipartFile> uploadFiles;
    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;

    public AdService(AdRepository adRepository, AdImageRepository adImageRepository, UserRepository userRepository,
                     ReviewRepository reviewRepository, EventRepository eventRepository, EmailService emailService) {
        this.adRepository = adRepository;
        this.adImageRepository = adImageRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.eventRepository = eventRepository;
        this.emailService = emailService;
    }

    private void saveImages(List<MultipartFile> images, Ad adId) {
        for (MultipartFile file : images) {
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

    public void setUploadFiles(List<MultipartFile> files) {
        uploadFiles = files;
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
        if (newAdDto.getUploadFiles().size() > 0) {
            this.saveImages(newAdDto.getUploadFiles(), newAd);
        }
    }

    public List<AdDto> getAllAdsWithFirstImage() {
        List<AdDto> adDtoList = new ArrayList<>();
        List<Ad> adList = adRepository.findAllByOrderByDateDesc();
        for (Ad i : adList) {
            System.out.println(i.getAdId());
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
            adDto.setLocation(i.getLocation());
            adDtoList.add(adDto);
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
        for (AdImage adImage : images) {
            String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(adImage.getImage());
            encodedImages.add(encodeImage);
        }
        return encodedImages;
    }

    public void deleteAdById(Long adId) {
        Ad ad = adRepository.findById(adId).get();
        List<User> userList = eventRepository.findAllUsersAtAd(ad);
        List<String> userEmails = new ArrayList<>();
        for (User i : userList) {
            i.setNotification(2L);
            userRepository.save(i);
            userEmails.add(i.getEmail());
            try {
                emailService.sendEmail(userEmails, "Programare anulata"
                        , "Cu parere de rau, te informam ca programarea ta a fost anulata" +
                                " deaoarece anuntul " + ad.getTitle() + " a fost sters!");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        adRepository.deleteById(adId);

    }
}
