package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;

@NoRepositoryBean
public interface RequestInfoRepository<T extends RequestInfo> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}