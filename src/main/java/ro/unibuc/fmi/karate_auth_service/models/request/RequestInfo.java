package ro.unibuc.fmi.karate_auth_service.models.request;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.BaseEntity;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@MappedSuperclass
@AllArgsConstructor
public abstract class RequestInfo extends BaseEntity {
    @Column(name = "request_type", nullable = false)
    protected RequestType type;

    @Column(name = "request_status", nullable = false)
    private RequestStatus status;

    @Column(name = "request_scope", nullable = false)
    private RequestScope scope;

    @Column(name = "created_by_id", nullable = false)
    private Long createdById;

    @Column(name = "last_updated_by_id", nullable = false)
    private Long lastUpdatedById;
}
