package epa.homefinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="pozeanunturi")
public class AdImage {

    @Id
    @Column(name = "idpozeanunt")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImg;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "idfanunt", nullable = false,
            foreignKey = @ForeignKey(name = "fk_poze_anunturi" ))
    private Ad adId;

    @Column(name = "poza")
    private byte[] image;
}
