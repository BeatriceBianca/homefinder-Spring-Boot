package epa.homefinder.dto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import epa.homefinder.entity.Event;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class EventDtoTransformer {

    public EventDto transform(Event event){
        EventDto eventDto = EventDto.builder()
                .adId(event.getAd().getAdId())
                .adTitle(event.getAd().getTitle())
                .userEmail(event.getUser().getEmail())
                .eventId(event.getEventId())
                .status(event.getStatus())
                .startDate(event.getStartDate().toString())
                .endDate(event.getEndDate().toString())
                .message(event.getMessage())
                .build();
        System.out.println(eventDto);
        return eventDto;
    }

    public EventDto transformOwner(Event event){
        EventDto eventDto = EventDto.builder()
                .adId(event.getAd().getAdId())
                .adTitle(event.getAd().getTitle())
                .userEmail(event.getUser().getEmail())
                .eventId(event.getEventId())
                .status(event.getStatus())
                .startDate(event.getStartDate().toString())
                .endDate(event.getEndDate().toString())
                .message(event.getMessage())
                .owner(true)
                .build();
        System.out.println(eventDto);
        return eventDto;
    }

    public List<EventDto> transformList(List<Event> events, Boolean owner) {
        if(!owner)
            return events.stream().map(this::transform).collect(Collectors.toList());
        else
            return events.stream().map(this::transformOwner).collect(Collectors.toList());
    }
}
