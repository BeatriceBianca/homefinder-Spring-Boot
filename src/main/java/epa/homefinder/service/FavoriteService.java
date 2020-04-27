package epa.homefinder.service;

import epa.homefinder.dao.*;
import epa.homefinder.dto.AdDto;
import epa.homefinder.dto.FavoriteDto;
import epa.homefinder.entity.Ad;
import epa.homefinder.entity.AdImage;
import epa.homefinder.entity.Favorite;
import epa.homefinder.entity.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final AdRepository adRepository;
    private final AdImageRepository adImageRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, AdRepository adRepository,
                           AdImageRepository adImageRepository, ReviewRepository reviewRepository,
                           EventRepository eventRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.adRepository = adRepository;
        this.adImageRepository = adImageRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
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
}
