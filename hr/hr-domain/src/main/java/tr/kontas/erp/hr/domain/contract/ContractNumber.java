package tr.kontas.erp.hr.domain.contract;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class ContractNumber extends ValueObject {

    private final String value;

    public ContractNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ContractNumber cannot be empty");
        }
        this.value = value;
    }
}
