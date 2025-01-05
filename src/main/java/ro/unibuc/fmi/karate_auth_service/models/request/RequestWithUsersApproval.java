package ro.unibuc.fmi.karate_auth_service.models.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@MappedSuperclass
@AllArgsConstructor
public abstract class RequestWithUsersApproval extends RequestInfo {

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "requests_assigned_users",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new LinkedHashSet<>();

}
