package tr.kontas.erp.hr.domain.jobapplication;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class JobApplicationId extends Identifier {
    private JobApplicationId(UUID value) {
        super(value);
    }

    public static JobApplicationId newId() {
        return new JobApplicationId(UUID.randomUUID());
    }

    public static JobApplicationId of(UUID value) {
        return new JobApplicationId(value);
    }

    public static JobApplicationId of(String value) {
        return new JobApplicationId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
