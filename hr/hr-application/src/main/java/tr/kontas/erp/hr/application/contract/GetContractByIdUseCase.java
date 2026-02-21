package tr.kontas.erp.hr.application.contract;

import tr.kontas.erp.hr.domain.contract.Contract;
import tr.kontas.erp.hr.domain.contract.ContractId;

public interface GetContractByIdUseCase {
    Contract execute(ContractId id);
}
