package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerformanceCyclePayload {
    private String id;
    private String companyId;
    private String name;
    private String startDate;
    private String endDate;
    private String reviewDeadline;
    private String status;
}
