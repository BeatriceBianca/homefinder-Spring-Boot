package epa.homefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import epa.homefinder.entity.Ad;
import epa.homefinder.entity.AdImage;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AdImageRepository extends JpaRepository<AdImage, Long> {

    List<AdImage> findAllByAdId(Ad adId);

    AdImage findFirstByAdId(Ad adId);

    @Transactional
    @Modifying
    @Query("delete from AdImage e where e.adId.adId = ?1")
    void deleteAllByAdId(Long adId);
}
