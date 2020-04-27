package epa.homefinder.controller;

import epa.homefinder.dto.EmailDto;
import epa.homefinder.dto.EventDto;
import epa.homefinder.dto.EventDtoDate;
import epa.homefinder.dto.ReportsDto;
import epa.homefinder.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/saveEvent")
    public void saveEvent(@RequestBody EventDtoDate eventDto) throws ParseException {
        eventService.saveEvent(eventDto);
    }

    @PostMapping("/getUserEvents")
    public List<EventDto> getUserEvents(@RequestBody EmailDto emailDto) {
        return eventService.getUserEvents(emailDto);
    }

    @PostMapping("/getAdEvents")
    public List<EventDto> getAdEvents(@RequestBody Long adId) {
        return eventService.getAdEvents(adId);
    }

    @PostMapping("/updateEvent")
    public void updateEvent(@RequestBody EventDtoDate eventDto) throws ParseException {
        eventService.updateEvent(eventDto);
    }

    @PostMapping("/deleteEvent")
    public void deleteEvent(@RequestBody EventDtoDate eventDto) {
        eventService.deleteEvent(eventDto);
    }

    @GetMapping("/eventsReport")
    public ReportsDto getEventsReport() {
        return eventService.getEventsReport();
    }
}
