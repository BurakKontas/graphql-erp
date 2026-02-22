package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateScheduledReportInput {
    private String reportDefinitionId;
    private String name;
    private String cronExpression;
    private String format;
    private List<String> recipientEmails;
}

