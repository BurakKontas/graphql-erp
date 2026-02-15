package tr.kontas.erp.core.platform.persistence.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.Company;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.company.CompanyRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepository {

    private final JpaCompanyRepository jpaRepository;

    @Override
    public void save(Company company) {
        jpaRepository.save(CompanyMapper.toEntity(company));
    }

    @Override
    public Optional<Company> findById(CompanyId id) {
        return jpaRepository.findById(id.asUUID())
                .map(CompanyMapper::toDomain);
    }

    @Override
    public List<Company> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(CompanyMapper::toDomain)
                .toList();
    }

    @Override
    public List<Company> findByTenant(TenantId tenantId) {
        return jpaRepository.findByTenantId(tenantId.asUUID())
                .stream()
                .map(CompanyMapper::toDomain)
                .toList();
    }

    @Override
    public List<Company> findByCompanyIds(List<CompanyId> ids) {
        List<UUID> uuids = ids.stream().map(CompanyId::asUUID).toList();

        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(CompanyMapper::toDomain)
                .toList();
    }

    @Override
    public Set<Company> findByTenantIds(List<TenantId> ids) {
        List<UUID> uuids = ids.stream().map(TenantId::asUUID).toList();

        return jpaRepository.findByTenantIdIn(uuids)
                .stream()
                .map(CompanyMapper::toDomain)
                .collect(Collectors.toSet());
    }
}
