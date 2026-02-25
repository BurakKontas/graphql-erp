package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reporting.validators.CreateScheduledReportInputValidator;

@Getter
@Setter
@NoArgsConstructor
@Validate(validator = CreateScheduledReportInputValidator.class)
public class CreateScheduledReportInput implements Validatable {
    private String reportDefinitionId;
    private String name;
    private String cronExpression;
    private String format;
    private List<String> recipientEmails;
}
