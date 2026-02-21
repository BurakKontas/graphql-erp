package tr.kontas.erp.hr.domain.payrollrun;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.event.PayrollPaidEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PayrollRun extends AggregateRoot<PayrollRunId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final PayrollRunNumber runNumber;
    private final int year;
    private final int month;
    private PayrollRunStatus status;
    private LocalDate paymentDate;
    private String payrollConfigId;
    private final List<PayrollEntry> entries;

    public PayrollRun(PayrollRunId id, TenantId tenantId, CompanyId companyId,
                      PayrollRunNumber runNumber, int year, int month,
                      PayrollRunStatus status, LocalDate paymentDate,
                      String payrollConfigId, List<PayrollEntry> entries) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.runNumber = runNumber;
        this.year = year;
        this.month = month;
        this.status = status;
        this.paymentDate = paymentDate;
        this.payrollConfigId = payrollConfigId;
        this.entries = new ArrayList<>(entries != null ? entries : List.of());
    }


    public List<PayrollEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }


    public void addEntry(PayrollEntry entry) {
        this.entries.add(entry);
    }


    public void markCalculated() {
        if (status != PayrollRunStatus.DRAFT) {
            throw new IllegalStateException("Can only calculate DRAFT payroll runs");
        }
        this.status = PayrollRunStatus.CALCULATED;
    }


    public void approve(String approverId) {
        if (status != PayrollRunStatus.CALCULATED) {
            throw new IllegalStateException("Can only approve CALCULATED payroll runs");
        }
        this.status = PayrollRunStatus.APPROVED;
    }


    public void markAsPaid() {
        if (status != PayrollRunStatus.APPROVED) {
            throw new IllegalStateException("Can only mark APPROVED payroll runs as paid");
        }
        this.status = PayrollRunStatus.PAID;
        registerEvent(new PayrollPaidEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                runNumber.getValue(), year, month));
    }


    public void cancel() {
        if (status != PayrollRunStatus.DRAFT && status != PayrollRunStatus.CALCULATED) {
            throw new IllegalStateException("Can only cancel DRAFT or CALCULATED payroll runs");
        }
        this.status = PayrollRunStatus.CANCELLED;
    }
}

