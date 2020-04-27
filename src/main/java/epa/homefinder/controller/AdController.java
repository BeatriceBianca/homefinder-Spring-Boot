package epa.homefinder.controller;

import epa.homefinder.dto.*;
import epa.homefinder.service.AdService;
import epa.homefinder.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class AdController {
    private final AdService adService;
    private final ReviewService reviewService;

    public AdController(AdService adService, ReviewService reviewService) {
        this.adService = adService;
        this.reviewService = reviewService;
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

        adService.setUploadFiles(images);
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
        adService.saveAdInfo(newAdDto);
    }

    @PostMapping("/replaceAdImages")
    public void replaceImages(@RequestParam("fileUpload") List<MultipartFile> images,
                              @RequestParam("adId") String adId) {
        adService.replaceImages(Long.parseLong(adId), images);
    }

    @PostMapping("/replaceAdInfo")
    public void replaceAdInfo(@RequestBody AdDto adDto) {
        adService.updateAdInfo(adDto);
    }

    @GetMapping("/adsWithImages")
    public List<AdDto> getAllAdsWithFirstImage() {
        return adService.getAllAdsWithFirstImage();
    }

    @PostMapping("/getAdInfo")
    public AdDetailsDto getAdInfo(@RequestBody Long adId) {
        return adService.getAdInfo(adId);
    }

    @PostMapping("/getAdImages")
    public List<String> getAdImages(@RequestBody Long adId) {
        return adService.getAdImages(adId);
    }

    @PostMapping("/deleteAd")
    public void deleteAd(@RequestBody Long adId) {
        adService.deleteAdById(adId);
    }

    @PostMapping("/getAdReviews")
    public List<ReviewDtoRequest> getReviews(@RequestBody Long adId) {
        return reviewService.getAdReviews(adId);
    }

    @PostMapping("/saveAdReview")
    public Long saveReview(@RequestBody ReviewDtoResponse reviewDtoResponse) {
        return reviewService.saveAdReview(reviewDtoResponse);
    }

    @PostMapping("/updateAdReview")
    public void updateReview(@RequestBody ReviewDtoResponse reviewDtoResponse) {
        reviewService.updateReview(reviewDtoResponse);
    }

    @PostMapping("/deleteAdReview")
    public void deleteAdReview(@RequestBody Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
