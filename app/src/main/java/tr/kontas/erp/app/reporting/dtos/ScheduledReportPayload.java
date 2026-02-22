package tr.kontas.erp.app.reporting.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledReportPayload {
    private String id;
    private String reportDefinitionId;
    private String name;
    private String cronExpression;
    private String format;
    private List<String> recipientEmails;
    private boolean active;
    private String lastRunAt;
    private String nextRunAt;
    private String createdBy;
}

