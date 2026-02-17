package tr.kontas.erp.core.domain.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LdapSettings extends ValueObject {
    private List<String> urls;
    private String baseDn;
    private String userSearchFilter;
    private String bindDn;
    private String bindPassword;
    @Builder.Default
    private int connectTimeoutMs = 5000;
    @Builder.Default
    private int readTimeoutMs = 5000;
    @Builder.Default
    private boolean startTls = false;
    @Builder.Default
    private int maxRetry = 2;
    @Builder.Default
    private long circuitBreakerTimeoutMs = 30000;
    @Builder.Default
    private boolean resolveGroups = false;
    @Builder.Default
    private String groupSearchFilter = "(member={0})";

    public boolean isConfigured() {
        return urls != null && !urls.isEmpty()
                && baseDn != null && !baseDn.isBlank();
    }
}
