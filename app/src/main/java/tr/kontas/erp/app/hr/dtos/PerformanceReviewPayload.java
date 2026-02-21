package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerformanceReviewPayload {
    private String id;
    private String companyId;
    private String cycleId;
    private String employeeId;
    private String reviewerId;
    private String status;
    private int overallRating;
    private String strengths;
    private String improvements;
    private String comments;
}
