package tr.kontas.erp.hr.application.contract;

import tr.kontas.erp.hr.domain.contract.ContractId;

public interface CreateContractUseCase {
    ContractId execute(CreateContractCommand command);
}
