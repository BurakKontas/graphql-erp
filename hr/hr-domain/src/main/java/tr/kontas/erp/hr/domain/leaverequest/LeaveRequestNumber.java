package tr.kontas.erp.hr.domain.leaverequest;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class LeaveRequestNumber extends ValueObject {

    private final String value;

    public LeaveRequestNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("LeaveRequestNumber cannot be empty");
        }
        this.value = value;
    }
}
