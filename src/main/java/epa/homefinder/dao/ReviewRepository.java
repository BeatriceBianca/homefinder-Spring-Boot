package epa.homefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import epa.homefinder.entity.Ad;
import epa.homefinder.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByAdOrderByIdDesc(Ad ad);

    @Query("select avg(r.rating) from Review r where r.ad = ?1")
    Double selectAvgReview(Ad ad);
}
