package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayrollRunPayload {
    private String id;
    private String companyId;
    private String runNumber;
    private int year;
    private int month;
    private String status;
    private String paymentDate;
    private String payrollConfigId;
}
