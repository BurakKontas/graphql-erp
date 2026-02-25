package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreatePositionInputValidator;

@Data
@Validate(validator = CreatePositionInputValidator.class)
public class CreatePositionInput implements Validatable {
    private String companyId;
    private String code;
    private String title;
    private String departmentId;
    private String level;
    private String salaryGrade;
    private int headcount;
}
