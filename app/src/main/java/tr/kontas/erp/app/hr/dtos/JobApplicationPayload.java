package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobApplicationPayload {
    private String id;
    private String companyId;
    private String jobPostingId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String cvRef;
    private String status;
    private String currentStageNote;
    private String appliedAt;
}
