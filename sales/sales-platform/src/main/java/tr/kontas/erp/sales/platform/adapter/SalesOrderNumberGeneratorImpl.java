package tr.kontas.erp.sales.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.application.port.SalesOrderNumberGeneratorPort;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderNumber;
import tr.kontas.erp.sales.platform.persistence.salesorder.JpaSalesOrderRepository;

@Component
@RequiredArgsConstructor
public class SalesOrderNumberGeneratorImpl implements SalesOrderNumberGeneratorPort {

    private final JpaSalesOrderRepository jpaRepository;

    @Override
    public SalesOrderNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "SO-%04d-%06d".formatted(year, nextSeq);
        return new SalesOrderNumber(value);
    }
}
