package epa.homefinder.service;

import epa.homefinder.dao.AdRepository;
import epa.homefinder.dao.EventRepository;
import epa.homefinder.dao.UserRepository;
import epa.homefinder.dto.*;
import epa.homefinder.entity.Ad;
import epa.homefinder.entity.Event;
import epa.homefinder.entity.User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EventService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;
    private final EventDtoTransformer eventDtoTransformer;

    public EventService(AdRepository adRepository, UserRepository userRepository,
                        EventRepository eventRepository, EmailService emailService,
                        EventDtoTransformer eventDtoTransformer) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.emailService = emailService;
        this.eventDtoTransformer = eventDtoTransformer;
    }

    public void saveEvent(EventDtoDate eventDto) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        Event event = new Event();
        Ad ad = adRepository.findById(eventDto.getAdId()).get();
        event.setAd(ad);
        event.setUser(userRepository.findByEmail(eventDto.getUserEmail()));
        event.setStatus(eventDto.getStatus());
        event.setMessage(eventDto.getMessage());
        event.setStartDate(simpleDateFormat.parse(eventDto.getStartDate()));
        event.setEndDate(simpleDateFormat.parse(eventDto.getEndDate()));
        eventRepository.save(event);
        User user = eventRepository.findOwner(adRepository.findById(eventDto.getAdId()).get());
        user.setNotification(3L);
        userRepository.save(user);
        List<String> receivers = new ArrayList<>();
        receivers.add(eventDto.getUserEmail());
        try {
            emailService.sendEmail(receivers, "Programare vizita", "Programarea vizitei la " + ad.getTitle()
                    + " a fost inregistrata cu succes!");
            receivers.remove(0);
            receivers.add(user.getEmail());
            emailService.sendEmail(receivers, "Programare noua", "Buna " + user.getEmail() + "!"
                    + "\n" + "O noua programare la " + ad.getTitle() + " asteapta confirmarea ta!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public List<EventDto> getUserEvents(EmailDto emailDto) {
        User user = userRepository.findByEmail(emailDto.getEmail());
        List<EventDto> eventDtos = eventDtoTransformer.transformList(user.getEvents(), false);
        if (eventDtos.size() == 0) {
            eventDtos = eventDtoTransformer.transformList(eventRepository.findAllByAdUserId(user), true);
        }
        return eventDtos;
    }

    public List<EventDto> getAdEvents(Long adId) {
        return eventDtoTransformer.transformList(adRepository.findById(adId).get().getEvents(), false);
    }

    public void deleteEvent(EventDtoDate eventDto) {
        Ad ad = adRepository.findById(eventDto.getAdId()).get();
        Event event = eventRepository.findByUserAndAd(
                userRepository.findByEmail(eventDto.getUserEmail()), ad);
        eventRepository.deleteByUserAndAd(
                userRepository.findByEmail(eventDto.getUserEmail()), ad);
        User user = userRepository.findUserByEmail(eventDto.getUserEmail());
        user.setNotification(2L);
        userRepository.save(user);
        List<String> users = new ArrayList<>();
        users.add(user.getEmail());
        try {
            emailService.sendEmail(users, "Update programare " + ad.getTitle(),
                    "Buna " + event.getUser().getUsername() + "!" + "\n" + "Cu parere de rau te informam ca programarea ta a fost refuzata.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void updateEvent(EventDtoDate eventDto) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        Ad ad = adRepository.findById(eventDto.getAdId()).get();
        Event event = eventRepository.findByUserAndAd(
                userRepository.findByEmail(eventDto.getUserEmail()), ad);
        event.setStatus(eventDto.getStatus());
        event.setMessage(eventDto.getMessage());
        event.setStartDate(simpleDateFormat.parse(eventDto.getStartDate()));
        event.setEndDate(simpleDateFormat.parse(eventDto.getEndDate()));
        User user = userRepository.findUserByEmail(eventDto.getUserEmail());
        user.setNotification(1L);
        userRepository.save(user);
        eventRepository.save(event);
        List<String> users = new ArrayList<>();
        users.add(user.getEmail());
        try {
            emailService.sendEmail(users, "Update programare " + ad.getTitle(),
                    "Buna " + event.getUser().getUsername() + "!" + "\n" + "Dorim sa te informam ca programarea ta a fost acceptata.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public ReportsDto getEventsReport() {
        Long adsValue = adRepository.count();
        return ReportsDto.builder()
                .acceptedEvents(eventRepository.getAcceptedEventsNumber())
                .pendingEvents(eventRepository.getPendingEventsNumber())
                .allEvents(eventRepository.getEventsTotal())
                .allAds(adsValue.intValue())
                .apartmentAds(adRepository.getApartmentAdsNumber())
                .homeAds(adRepository.getHomeAdsNumber())
                .rentAds(adRepository.getRentAdsNumber())
                .salesAds(adRepository.getSalesAdsNumber())
                .build();
    }
}
