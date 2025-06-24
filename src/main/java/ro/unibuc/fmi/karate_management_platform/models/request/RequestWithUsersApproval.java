package ro.unibuc.fmi.karate_management_platform.models.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class RequestWithUsersApproval extends RequestInfo {
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "request_approver_users",
            joinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id", table = "request_info"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    @Column(name = "approver_users", nullable = false)
    private Set<User> approverUsers = new LinkedHashSet<>();
}
