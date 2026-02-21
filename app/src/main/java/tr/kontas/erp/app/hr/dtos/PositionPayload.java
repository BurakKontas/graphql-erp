package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PositionPayload {
    private String id;
    private String companyId;
    private String code;
    private String title;
    private String departmentId;
    private String level;
    private String salaryGrade;
    private int headcount;
    private int filledCount;
    private String status;
}
