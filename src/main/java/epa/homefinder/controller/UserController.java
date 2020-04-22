package epa.homefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import epa.homefinder.dao.AdImageRepository;
import epa.homefinder.dao.AdRepository;
import epa.homefinder.dto.*;
import epa.homefinder.service.UserService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private AdImageRepository adImageRepository;
    @Autowired
    private AdRepository adRepository;
    private UserService userService;
    private List<Map<String, String>> mapList = new ArrayList<>();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public void userRegistration(@RequestBody UserRegistrationDto userRegistrationDto){
        System.out.println(userRegistrationDto);
        userService.registerNewUser(userRegistrationDto);
    }

    @GetMapping("/userList")
    public List<UserDataDto> getUsersData() {
        return userService.getUsersData();
    }

    @PostMapping("/updateUser")
    public void  updateUser(@RequestBody UserDataDto userDataDto) {
        userService.updateUser(userDataDto);
    }

    @PostMapping("/getUserData")
    public UserDto getUserData(@RequestBody EmailDto emailDto) {
        return userService.getUserData(emailDto);
    }

    @PostMapping("/updateUserData")
    public void updateUserData(@RequestBody UserDtoUpdate userDtoUpdate) {
        userService.updateUserData(userDtoUpdate);
    }

    @PostMapping(value="/newAdImages" , consumes = {"multipart/form-data"})
    public void saveNewAdImages(@RequestParam("fileUpload") List<MultipartFile> images,
                                @RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("adItemType") String adItemType,
                                @RequestParam("adType") String adType,
                                @RequestParam("price") String price,
                                @RequestParam("rooms") String rooms,
                                @RequestParam("surface") String surface,
                                @RequestParam("lat") String lat,
                                @RequestParam("lng") String lng,
                                @RequestParam("userEmail") String userEmail,
                                @RequestParam("partitioning") String partitioning,
                                @RequestParam("comfort") Integer comfort,
                                @RequestParam("floorLevel") String floorLevel,
                                @RequestParam("areaSurface") String areaSurface,
                                @RequestParam("yearBuilt") Integer yearBuilt,
                                @RequestParam("furnished") String furnished,
                                @RequestParam("location") String location) throws IOException {

        userService.setUploadFiles(images);
        NewAdDto newAdDto = new NewAdDto();
        newAdDto.setTitle(title);
        newAdDto.setDescription(description);
        newAdDto.setAdItemType(adItemType);
        newAdDto.setAdType(adType);
        newAdDto.setPrice(Double.parseDouble(price));
        newAdDto.setRooms(Integer.parseInt(rooms));
        newAdDto.setSurface(Double.parseDouble(surface));
        newAdDto.setLat(Double.parseDouble(lat));
        newAdDto.setLng(Double.parseDouble(lng));
        newAdDto.setUserEmail(userEmail);
        newAdDto.setUploadFiles(images);
        newAdDto.setPartitioning(partitioning);
        newAdDto.setComfort(comfort);
        newAdDto.setFloorLevel(floorLevel);
        newAdDto.setAreaSurface(Double.parseDouble(areaSurface));
        newAdDto.setFurnished(furnished);
        newAdDto.setYearBuilt(yearBuilt);
        newAdDto.setLocation(location);
        userService.saveAdInfo(newAdDto);
    }

    @PostMapping("/replaceAdImages")
    public void replaceImages(@RequestParam("fileUpload") List<MultipartFile> images,
                              @RequestParam("adId") String adId) {
        userService.replaceImages(Long.parseLong(adId), images);
    }

    @PostMapping("/replaceAdInfo")
    public void replaceAdInfo(@RequestBody AdDto adDto) {
        userService.updateAdInfo(adDto);
    }

    @PostMapping("/getFavoriteAds")
    public List<AdDto> getFavouriteAds(@RequestBody EmailDto userEmail) {
        return userService.getAllFavorites(userEmail.getEmail());
    }

    @PostMapping("/saveFavorite")
    public void saveFavorite(@RequestBody FavoriteDto favoriteDto) {
        userService.saveFavorite(favoriteDto);
    }

    @GetMapping("/adsWithImages")
    public List<AdDto> getAllAdsWithFirstImage() {
        return userService.getAllAdsWithFirstImage();
    }

    @PostMapping("/getUserAds")
    public List<AdDto> getUserAds(@RequestBody EmailDto userEmail) {
        return userService.getUserAds(userEmail);
    }

    @PostMapping("/getAdInfo")
    public AdDetailsDto getAdInfo(@RequestBody Long adId) {
        return userService.getAdInfo(adId);
    }

    @PostMapping("/getAdImages")
    public List<String> getAdImages(@RequestBody Long adId) {
        return userService.getAdImages(adId);
    }

    @PostMapping("/deleteAd")
    public void deleteAd(@RequestBody Long adId) {
        userService.deleteAdById(adId);
    }

    @PostMapping("/getAdReviews")
    public List<ReviewDtoRequest> getReviews(@RequestBody Long adId) {
        return userService.getAdReviews(adId);
    }

    @PostMapping("/saveAdReview")
    public Long saveReview(@RequestBody ReviewDtoResponse reviewDtoResponse) {
        return userService.saveAdReview(reviewDtoResponse);
    }

    @PostMapping("/updateAdReview")
    public void updateReview(@RequestBody ReviewDtoResponse reviewDtoResponse) {
        userService.updateReview(reviewDtoResponse);
    }

    @PostMapping("/deleteAdReview")
    public void deleteAdReview(@RequestBody Long reviewId) {
        userService.deleteReview(reviewId);
    }

    @PostMapping("/deleteFavorite")
    public void deleteFavorite(@RequestBody Long favoriteId) {
        userService.deleteFavorite(favoriteId);
    }

    @GetMapping("/getUserEmails")
    public List<String> getUserEmails() {
        return userService.getUserEmails();
    }

    @PostMapping("/saveEvent")
    public void saveEvent(@RequestBody EventDtoDate eventDto) throws ParseException {
        userService.saveEvent(eventDto);
    }

    @PostMapping("/getUserEvents")
    public List<EventDto> getUserEvents(@RequestBody EmailDto emailDto) {
        return userService.getUserEvents(emailDto);
    }

    @PostMapping("/getAdEvents")
    public List<EventDto> getAdEvents(@RequestBody Long adId) {
        return userService.getAdEvents(adId);
    }

    @PostMapping("/updateEvent")
    public void updateEvent(@RequestBody EventDtoDate eventDto) throws ParseException {
        userService.updateEvent(eventDto);
    }

    @PostMapping("/deleteEvent")
    public void deleteEvent(@RequestBody EventDtoDate eventDto) {
        userService.deleteEvent(eventDto);
    }

    @GetMapping("/eventsReport")
    public ReportsDto getEventsReport() {
        return userService.getEventsReport();
    }
}
