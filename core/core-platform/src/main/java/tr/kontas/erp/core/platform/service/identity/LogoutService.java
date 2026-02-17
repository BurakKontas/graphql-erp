package tr.kontas.erp.core.platform.service.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.application.identity.LogoutUseCase;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void execute(UserId userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Increment auth version to invalidate all existing tokens
        user.incrementAuthVersion();

        // Save updated user
        userRepository.save(user);
    }
}
