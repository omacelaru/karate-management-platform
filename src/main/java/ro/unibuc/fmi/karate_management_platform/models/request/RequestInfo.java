package ro.unibuc.fmi.karate_management_platform.models.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.BaseEntity;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "request_info")
public abstract class RequestInfo extends BaseEntity {
    @Column(name = "request_type", nullable = false)
    @Enumerated(EnumType.STRING)
    protected RequestType type;

    @Column(name = "request_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "request_scope", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestScope scope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    @ToString.Exclude
    private User createdBy;

    @Column(name = "last_updated_by_id", nullable = false)
    private Long lastUpdatedById;
}
