package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreateJobApplicationInput {
    private String companyId;
    private String jobPostingId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String cvRef;
}
