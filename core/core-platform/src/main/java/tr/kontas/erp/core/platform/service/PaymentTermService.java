package tr.kontas.erp.core.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.reference.payment.CreatePaymentTermCommand;
import tr.kontas.erp.core.application.reference.payment.CreatePaymentTermUseCase;
import tr.kontas.erp.core.application.reference.payment.GetPaymentTermsByCompanyIdsUseCase;
import tr.kontas.erp.core.application.reference.payment.GetPaymentTermsByCompanyUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.payment.PaymentTermCode;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.platform.persistence.reference.payment.PaymentTermRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentTermService implements
        CreatePaymentTermUseCase,
        GetPaymentTermsByCompanyUseCase,
        GetPaymentTermsByCompanyIdsUseCase {

    private final PaymentTermRepositoryImpl paymentTermRepository;

    @Override
    @Transactional
    public PaymentTermCode execute(CreatePaymentTermCommand command) {
        TenantId tenantId = TenantContext.get();
        PaymentTermCode code = new PaymentTermCode(command.code());

        if (paymentTermRepository.findByCode(tenantId, command.companyId(), code).isPresent()) {
            throw new IllegalArgumentException("PaymentTerm with code " + command.code() + " already exists");
        }

        PaymentTerm paymentTerm = new PaymentTerm(
                code,
                tenantId,
                command.companyId(),
                command.name(),
                command.dueDays()
        );

        paymentTermRepository.save(paymentTerm);

        return code;
    }

    @Override
    public List<PaymentTerm> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return paymentTermRepository.findByCompany(tenantId, companyId);
    }

    @Override
    @Transactional
    public Map<CompanyId, List<PaymentTerm>> executeByCompanyIds(List<CompanyId> companyIds) {
        TenantId tenantId = TenantContext.get();
        Map<CompanyId, List<PaymentTerm>> resultMap = new HashMap<>();

        List<PaymentTerm> paymentTerms = paymentTermRepository.findByCompanyIds(tenantId, companyIds);

        paymentTerms.forEach(pt -> {
            resultMap.computeIfAbsent(pt.getCompanyId(), _ -> new ArrayList<>())
                    .add(pt);
        });

        return resultMap;
    }
}
