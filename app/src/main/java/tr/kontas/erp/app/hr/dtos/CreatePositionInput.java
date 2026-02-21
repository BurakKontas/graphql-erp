package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreatePositionInput {
    private String companyId;
    private String code;
    private String title;
    private String departmentId;
    private String level;
    private String salaryGrade;
    private int headcount;
}
