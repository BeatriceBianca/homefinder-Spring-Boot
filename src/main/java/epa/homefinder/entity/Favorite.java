package epa.homefinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @Column(name = "idfavorites")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long favoritesId;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false,
            foreignKey = @ForeignKey(name = "fk_favorites_users"))
    private User user;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_anunt", nullable = false,
            foreignKey = @ForeignKey(name = "fk_favorites_ads"))
    private Ad ad;
}
