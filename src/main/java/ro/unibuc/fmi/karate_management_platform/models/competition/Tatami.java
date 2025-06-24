package ro.unibuc.fmi.karate_management_platform.models.competition;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import ro.unibuc.fmi.karate_management_platform.models.BaseEntity;
import ro.unibuc.fmi.karate_management_platform.models.referee.Referee;

import java.util.Set;


public class Tatami extends BaseEntity {

    @Column(name = "number", nullable = false)
    private short number;

    @ManyToMany
    @JoinTable(
            name = "tatami_referees",
            joinColumns = @JoinColumn(name = "tatami_id"),
            inverseJoinColumns = @JoinColumn(name = "referee_id")
    )
    private Set<Referee> referees;
}
