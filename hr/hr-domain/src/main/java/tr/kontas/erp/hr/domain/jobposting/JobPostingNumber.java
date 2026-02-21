package tr.kontas.erp.hr.domain.jobposting;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class JobPostingNumber extends ValueObject {

    private final String value;

    public JobPostingNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("JobPostingNumber cannot be empty");
        }
        this.value = value;
    }
}
