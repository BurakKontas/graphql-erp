package tr.kontas.erp.hr.application.jobapplication;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreateJobApplicationCommand(CompanyId companyId, String jobPostingId, String firstName, String lastName, String email, String phone, String cvRef) {}
