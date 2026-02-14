package tr.kontas.erp.core.domain.tenant;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class TenantName extends ValueObject {
    private final String value;

    public TenantName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tenant name cannot be empty");
        }
        this.value = value;
    }

}
