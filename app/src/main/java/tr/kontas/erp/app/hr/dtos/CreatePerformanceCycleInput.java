package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreatePerformanceCycleInput {
    private String companyId;
    private String name;
    private String startDate;
    private String endDate;
    private String reviewDeadline;
}
