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
@AllArgsConstructor
@MappedSuperclass
public class RequestWithUsersApproval extends RequestInfo {
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "requests_approver_users",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    @Column(name = "approver_users", nullable = false)
    private Set<User> users = new LinkedHashSet<>();
}
