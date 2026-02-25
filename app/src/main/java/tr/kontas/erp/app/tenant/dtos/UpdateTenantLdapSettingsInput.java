package tr.kontas.erp.app.tenant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.tenant.validators.UpdateTenantLdapSettingsInputValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validate(validator = UpdateTenantLdapSettingsInputValidator.class)
public class UpdateTenantLdapSettingsInput implements Validatable {
    private String tenantId;
    private List<String> urls;
    private String baseDn;
    private String userSearchFilter;
    private String bindDn;
    private String bindPassword;
    private Integer connectTimeoutMs;
    private Integer readTimeoutMs;
    private Boolean startTls;
    private Integer maxRetry;
    private Long circuitBreakerTimeoutMs;
    private Boolean resolveGroups;
    private String groupSearchFilter;
}
