package tr.kontas.erp.hr.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PayrollPaidEvent extends DomainEvent {

    private UUID payrollRunId;
    private UUID tenantId;
    private UUID companyId;
    private String runNumber;
    private int year;
    private int month;

    @Override
    public UUID getAggregateId() {
        return payrollRunId;
    }


    @Override
    public String getAggregateType() {
        return "PayrollRun";
    }
}
