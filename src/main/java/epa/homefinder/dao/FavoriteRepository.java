package epa.homefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import epa.homefinder.entity.Ad;
import epa.homefinder.entity.Favorite;
import epa.homefinder.entity.User;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select a from Favorite f join f.ad a where f.user = ?1")
    List<Ad> getAllFavouriteAdsByUser(User user);

    void deleteByAdAdId(Long ad_adId);
}
