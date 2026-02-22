package tr.kontas.erp.reporting.application.scheduled;

import java.util.List;

public record CreateScheduledReportCommand(
        String reportDefinitionId,
        String name,
        String cronExpression,
        String format,
        List<String> recipientEmails
) {}

