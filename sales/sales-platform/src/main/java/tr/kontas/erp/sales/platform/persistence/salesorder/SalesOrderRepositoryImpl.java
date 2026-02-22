package tr.kontas.erp.sales.platform.persistence.salesorder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.application.port.TaxResolutionPort;
import tr.kontas.erp.sales.domain.salesorder.SalesOrder;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderNumber;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SalesOrderRepositoryImpl implements SalesOrderRepository {

    private final JpaSalesOrderRepository jpaRepository;
    private final TaxResolutionPort taxResolution;

    @Override
    public void save(SalesOrder order) {
        SalesOrderJpaEntity entity = SalesOrderMapper.toEntity(order);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<SalesOrder> findById(SalesOrderId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(this::mapToDomain);
    }

    @Override
    public Optional<SalesOrder> findByOrderNumber(SalesOrderNumber orderNumber, TenantId tenantId) {
        return jpaRepository.findByOrderNumberAndTenantId(orderNumber.getValue(), tenantId.asUUID())
                .map(this::mapToDomain);
    }

    @Override
    public boolean existsByOrderNumber(SalesOrderNumber orderNumber, TenantId tenantId) {
        return jpaRepository.existsByOrderNumberAndTenantId(orderNumber.getValue(), tenantId.asUUID());
    }

    @Override
    public List<SalesOrder> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalesOrder> findDraftsByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyIdAndStatus(tenantId.asUUID(), companyId.asUUID(), "DRAFT")
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalesOrder> findByIds(List<SalesOrderId> ids) {
        List<UUID> uuids = ids.stream()
                .map(SalesOrderId::asUUID)
                .collect(Collectors.toList());

        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }


    private SalesOrder mapToDomain(SalesOrderJpaEntity entity) {
        TenantId tenantId = TenantId.of(entity.getTenantId());
        CompanyId companyId = CompanyId.of(entity.getCompanyId());

        Currency currency = null;
        if (entity.getCurrencyCode() != null) {
            try {
                currency = taxResolution.resolveCurrency(entity.getCurrencyCode());
            } catch (Exception e) {
                log.warn("Failed to resolve currency '{}': {}", entity.getCurrencyCode(), e.getMessage());
            }
        }

        PaymentTerm paymentTerm = null;
        if (entity.getPaymentTermCode() != null) {
            try {
                paymentTerm = taxResolution.resolvePaymentTerm(tenantId, companyId, entity.getPaymentTermCode());
            } catch (Exception e) {
                log.warn("Failed to resolve payment term '{}': {}", entity.getPaymentTermCode(), e.getMessage());
            }
        }

        Set<String> taxCodes = entity.getLines().stream()
                .map(SalesOrderLineJpaEntity::getTaxCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<String, Tax> taxMap = new HashMap<>();
        for (String taxCode : taxCodes) {
            try {
                Tax tax = taxResolution.resolveTax(tenantId, companyId, taxCode);
                taxMap.put(taxCode, tax);
            } catch (Exception e) {
                log.warn("Failed to resolve tax '{}': {}", taxCode, e.getMessage());
            }
        }

        return SalesOrderMapper.toDomain(entity, currency, paymentTerm, taxMap);
    }
}
