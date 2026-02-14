package tr.kontas.erp.core.platform.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Context {
    private final String userId;
    private final String role;
    private final UUID tenantId;
}
