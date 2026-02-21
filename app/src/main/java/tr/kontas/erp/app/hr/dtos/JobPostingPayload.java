package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobPostingPayload {
    private String id;
    private String companyId;
    private String postingNumber;
    private String positionId;
    private String title;
    private String description;
    private String employmentType;
    private String status;
    private String publishedAt;
    private String closingDate;
}
