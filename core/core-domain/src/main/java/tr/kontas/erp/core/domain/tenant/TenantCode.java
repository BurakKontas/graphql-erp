package tr.kontas.erp.core.domain.tenant;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class TenantCode extends ValueObject {
    private final String value;

    public TenantCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tenant code cannot be empty");
        }
        this.value = value;
    }

}