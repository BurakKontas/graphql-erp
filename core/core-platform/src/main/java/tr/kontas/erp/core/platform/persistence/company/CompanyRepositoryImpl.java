package tr.kontas.erp.core.platform.persistence.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.*;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
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
                .collect(Collectors.toList());
    }
}
