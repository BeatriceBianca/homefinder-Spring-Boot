package epa.homefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import epa.homefinder.entity.Ad;
import epa.homefinder.entity.Event;
import epa.homefinder.entity.User;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    void deleteByUserAndAd(User user, Ad ad);
    Event findByUserAndAd(User user, Ad ad);
    @Query("select e.user from Event e where e.ad = ?1")
    List<User> findAllUsersAtAd(Ad ad);
    @Query("select e.ad.userId from Event e where e.ad = ?1")
    User findOwner(Ad ad);
    List<Event> findAllByAdUserId(User ad_userId);
    @Query("select count(e) from Event e where e.status like 'PENDING'")
    Integer getPendingEventsNumber();
    @Query("select count(e) from Event e where e.status like 'ACCEPTED'")
    Integer getAcceptedEventsNumber();
    @Query("select count(e) from Event e")
    Integer getEventsTotal();
}
