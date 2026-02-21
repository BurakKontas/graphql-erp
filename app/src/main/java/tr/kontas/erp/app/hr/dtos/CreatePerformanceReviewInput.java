package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreatePerformanceReviewInput {
    private String companyId;
    private String cycleId;
    private String employeeId;
    private String reviewerId;
}
