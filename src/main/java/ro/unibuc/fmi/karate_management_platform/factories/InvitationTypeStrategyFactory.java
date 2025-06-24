package ro.unibuc.fmi.karate_management_platform.factories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.invitation.InvitationType;
import ro.unibuc.fmi.karate_management_platform.strategies.invitationType.InvitationTypeStrategy;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InvitationTypeStrategyFactory {
    private final Map<InvitationType, InvitationTypeStrategy> invitationTypeStrategyMap;

    @Autowired
    public InvitationTypeStrategyFactory(List<InvitationTypeStrategy> strategies) {
        invitationTypeStrategyMap = strategies.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                InvitationTypeStrategy::getInvitationType,
                                strategy -> strategy
                        )
                );
    }

    public InvitationTypeStrategy getStrategy(InvitationType invitationType) {
        InvitationTypeStrategy strategy = invitationTypeStrategyMap.get(invitationType);
        if (strategy == null) {
            log.error("No strategy found for invitation type: {}", invitationType);
            throw new IllegalArgumentException("No strategy found for invitation type: " + invitationType);
        }
        return strategy;
    }
}
