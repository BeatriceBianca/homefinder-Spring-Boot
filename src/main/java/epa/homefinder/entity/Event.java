package epa.homefinder.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "idevents")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iduser", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "idad", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ad_id"))
    private Ad ad;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end")
    private Date endDate;

    @Column(name="message")
    private String message;

    @Column(name="status")
    private String status;

}
