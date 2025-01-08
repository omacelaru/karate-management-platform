package ro.unibuc.fmi.karate_auth_service.models.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.BaseEntity;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@MappedSuperclass
@AllArgsConstructor
public abstract class RequestInfo extends BaseEntity {
    //TODO - create joined entity for request info
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
