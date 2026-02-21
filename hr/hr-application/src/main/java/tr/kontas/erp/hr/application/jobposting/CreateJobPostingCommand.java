package tr.kontas.erp.hr.application.jobposting;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.time.LocalDate;
import java.util.List;

public record CreateJobPostingCommand(CompanyId companyId, String positionId, String title, String description, String employmentType, LocalDate closingDate, List<RequirementInput> requirements) {
    public record RequirementInput(String type, String description, boolean mandatory) {}
}
