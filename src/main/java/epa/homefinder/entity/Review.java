package epa.homefinder.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "idreview")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false,
                foreignKey = @ForeignKey(name = "fk_review_users"))
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_anunt", nullable = false,
                foreignKey = @ForeignKey(name = "fk_review_anunturi"))
    private Ad ad;

    @Column(name = "review_comment")
    private String comment;

    @Column(name = "review_like")
    private Integer like;

    @Column(name = "review_rating")
    private Integer rating;

    @Column(name = "date_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
}
