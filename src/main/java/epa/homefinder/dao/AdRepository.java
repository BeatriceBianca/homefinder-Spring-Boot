package epa.homefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import epa.homefinder.entity.Ad;
import epa.homefinder.entity.User;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findAllByOrderByDateDesc();
    List<Ad> findAllByUserIdOrderByDateDesc(User userId);
    @Query("select count(a) from Ad a where a.adType like 'Casa'")
    Integer getHomeAdsNumber();
    @Query("select count(a) from Ad a where a.adType like 'Apartament'")
    Integer getApartmentAdsNumber();
    @Query("select count(a) from Ad a where a.adTranzactionType like 'Vanzare'")
    Integer getSalesAdsNumber();
    @Query("select count(a) from Ad a where a.adTranzactionType like 'Inchiriere'")
    Integer getRentAdsNumber();
}
