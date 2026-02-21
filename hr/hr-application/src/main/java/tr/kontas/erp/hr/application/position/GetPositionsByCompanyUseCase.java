package tr.kontas.erp.hr.application.position;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.position.Position;

import java.util.List;

public interface GetPositionsByCompanyUseCase {
    List<Position> execute(CompanyId companyId);
}
