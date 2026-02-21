package tr.kontas.erp.crm.application.activity;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.crm.domain.activity.Activity;

import java.util.List;

public interface GetActivitiesByCompanyUseCase {
    List<Activity> execute(CompanyId companyId);
}

