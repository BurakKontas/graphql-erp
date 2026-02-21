package tr.kontas.erp.app.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountingPeriodPayload {
    private String id;
    private String companyId;
    private String periodType;
    private String startDate;
    private String endDate;
    private String status;
}

