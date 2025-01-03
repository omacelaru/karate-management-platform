package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;

public interface RequestInfoRepository<T extends RequestInfo> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}