package tr.kontas.erp.hr.application.contract;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.contract.Contract;

import java.util.List;

public interface GetContractsByCompanyUseCase {
    List<Contract> execute(CompanyId companyId);
}
