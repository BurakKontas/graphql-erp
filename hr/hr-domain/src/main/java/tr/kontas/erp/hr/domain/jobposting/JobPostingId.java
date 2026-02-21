package tr.kontas.erp.hr.domain.jobposting;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class JobPostingId extends Identifier {
    private JobPostingId(UUID value) {
        super(value);
    }

    public static JobPostingId newId() {
        return new JobPostingId(UUID.randomUUID());
    }

    public static JobPostingId of(UUID value) {
        return new JobPostingId(value);
    }

    public static JobPostingId of(String value) {
        return new JobPostingId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
