package tr.kontas.erp.hr.application.contract;

import tr.kontas.erp.hr.domain.contract.Contract;
import tr.kontas.erp.hr.domain.contract.ContractId;

import java.util.List;

public interface GetContractsByIdsUseCase {
    List<Contract> execute(List<ContractId> ids);
}
