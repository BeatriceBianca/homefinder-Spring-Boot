package epa.homefinder.controller;

import epa.homefinder.dto.AdDto;
import epa.homefinder.dto.EmailDto;
import epa.homefinder.dto.FavoriteDto;
import epa.homefinder.service.FavoriteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/getFavoriteAds")
    public List<AdDto> getFavouriteAds(@RequestBody EmailDto userEmail) {
        return favoriteService.getAllFavorites(userEmail.getEmail());
    }

    @PostMapping("/saveFavorite")
    public void saveFavorite(@RequestBody FavoriteDto favoriteDto) {
        favoriteService.saveFavorite(favoriteDto);
    }

    @PostMapping("/deleteFavorite")
    public void deleteFavorite(@RequestBody Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
    }
}
