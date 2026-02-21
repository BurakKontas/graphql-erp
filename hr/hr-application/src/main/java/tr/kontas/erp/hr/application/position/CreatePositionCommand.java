package tr.kontas.erp.hr.application.position;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreatePositionCommand(CompanyId companyId, String code, String title, String departmentId, String level, String salaryGrade, int headcount) {}
