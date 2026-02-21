package tr.kontas.erp.app.finance.dtos;

import lombok.Data;

@Data
public class CreateAccountingPeriodInput {
    private String companyId;
    private String periodType;
    private String startDate;
    private String endDate;
}

