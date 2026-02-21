package tr.kontas.erp.hr.platform.persistence.contract;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaContractRepository extends JpaRepository<ContractJpaEntity, UUID> {
    List<ContractJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<ContractJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
