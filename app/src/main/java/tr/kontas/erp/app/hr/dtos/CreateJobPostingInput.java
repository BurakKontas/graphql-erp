package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreateJobPostingInput {
    private String companyId;
    private String positionId;
    private String title;
    private String description;
    private String employmentType;
    private String closingDate;
}
