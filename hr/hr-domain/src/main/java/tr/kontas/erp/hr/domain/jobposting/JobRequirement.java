package tr.kontas.erp.hr.domain.jobposting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobRequirement {
    private final RequirementType type;
    private final String description;
    private final boolean mandatory;
}
