package tr.kontas.erp.app.reporting.graphql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tr.kontas.erp.app.reporting.dtos.CreateReportDefinitionInput;
import tr.kontas.erp.app.reporting.dtos.CreateSavedReportInput;
import tr.kontas.erp.app.reporting.dtos.CreateScheduledReportInput;
import tr.kontas.erp.app.reporting.dtos.ReportDefinitionPayload;
import tr.kontas.erp.app.reporting.dtos.ReportResultPayload;
import tr.kontas.erp.app.reporting.dtos.RunReportInput;
import tr.kontas.erp.app.reporting.dtos.SavedReportPayload;
import tr.kontas.erp.app.reporting.dtos.ScheduledReportPayload;
import tr.kontas.erp.app.reporting.dtos.UpdateReportDefinitionInput;
import tr.kontas.erp.reporting.application.definition.CreateReportDefinitionCommand;
import tr.kontas.erp.reporting.application.definition.CreateReportDefinitionUseCase;
import tr.kontas.erp.reporting.application.definition.GetReportDefinitionByIdUseCase;
import tr.kontas.erp.reporting.application.definition.GetReportDefinitionsUseCase;
import tr.kontas.erp.reporting.application.definition.UpdateReportDefinitionCommand;
import tr.kontas.erp.reporting.application.definition.UpdateReportDefinitionUseCase;
import tr.kontas.erp.reporting.application.run.RunReportCommand;
import tr.kontas.erp.reporting.application.run.RunReportResult;
import tr.kontas.erp.reporting.application.run.RunReportUseCase;
import tr.kontas.erp.reporting.application.saved.CreateSavedReportCommand;
import tr.kontas.erp.reporting.application.saved.CreateSavedReportUseCase;
import tr.kontas.erp.reporting.application.saved.DeleteSavedReportUseCase;
import tr.kontas.erp.reporting.application.saved.GetSavedReportsUseCase;
import tr.kontas.erp.reporting.application.scheduled.CreateScheduledReportCommand;
import tr.kontas.erp.reporting.application.scheduled.CreateScheduledReportUseCase;
import tr.kontas.erp.reporting.application.scheduled.DeleteScheduledReportUseCase;
import tr.kontas.erp.reporting.application.scheduled.GetScheduledReportsUseCase;
import tr.kontas.erp.reporting.domain.definition.ReportDefinition;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;
import tr.kontas.erp.reporting.domain.saved.SavedReport;
import tr.kontas.erp.reporting.domain.scheduled.ScheduledReport;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class ReportingGraphql {

    private final GetReportDefinitionsUseCase getReportDefinitionsUseCase;
    private final GetReportDefinitionByIdUseCase getReportDefinitionByIdUseCase;
    private final CreateReportDefinitionUseCase createReportDefinitionUseCase;
    private final UpdateReportDefinitionUseCase updateReportDefinitionUseCase;
    private final RunReportUseCase runReportUseCase;
    private final GetSavedReportsUseCase getSavedReportsUseCase;
    private final CreateSavedReportUseCase createSavedReportUseCase;
    private final DeleteSavedReportUseCase deleteSavedReportUseCase;
    private final GetScheduledReportsUseCase getScheduledReportsUseCase;
    private final CreateScheduledReportUseCase createScheduledReportUseCase;
    private final DeleteScheduledReportUseCase deleteScheduledReportUseCase;
    private final ObjectMapper objectMapper;

    public static ReportDefinitionPayload toPayload(ReportDefinition d) {
        return new ReportDefinitionPayload(
                d.getId().toString(),
                d.getName(),
                d.getDescription(),
                d.getModule().name(),
                d.getType().name(),
                d.getDataSource(),
                d.getColumnsJson(),
                d.getFiltersJson(),
                d.getRequiredPermission(),
                d.isSystemReport(),
                d.isActive()
        );
    }

    public static SavedReportPayload toPayload(SavedReport s) {
        return new SavedReportPayload(
                s.getId().toString(),
                s.getReportDefinitionId().toString(),
                s.getName(),
                s.getSavedFiltersJson(),
                s.getSavedSortsJson(),
                s.isShared(),
                s.getCreatedBy(),
                s.getCreatedAt() != null ? s.getCreatedAt().toString() : null
        );
    }

    public static ScheduledReportPayload toPayload(ScheduledReport s) {
        return new ScheduledReportPayload(
                s.getId().toString(),
                s.getReportDefinitionId().toString(),
                s.getName(),
                s.getCronExpression(),
                s.getFormat().name(),
                s.getRecipientEmails(),
                s.isActive(),
                s.getLastRunAt() != null ? s.getLastRunAt().toString() : null,
                s.getNextRunAt() != null ? s.getNextRunAt().toString() : null,
                s.getCreatedBy()
        );
    }

    @DgsQuery
    public List<ReportDefinitionPayload> reportDefinitions() {
        return getReportDefinitionsUseCase.execute().stream().map(ReportingGraphql::toPayload).toList();
    }

    @DgsQuery
    public ReportDefinitionPayload reportDefinition(@InputArgument("id") String id) {
        return toPayload(getReportDefinitionByIdUseCase.execute(ReportDefinitionId.of(id)));
    }

    @DgsQuery
    public List<SavedReportPayload> savedReports(@InputArgument("userId") String userId) {
        return getSavedReportsUseCase.execute(userId).stream().map(ReportingGraphql::toPayload).toList();
    }

    @DgsQuery
    public List<ScheduledReportPayload> scheduledReports() {
        return getScheduledReportsUseCase.execute().stream().map(ReportingGraphql::toPayload).toList();
    }

    @DgsMutation
    public ReportDefinitionPayload createReportDefinition(@InputArgument("input") CreateReportDefinitionInput input) {
        var id = createReportDefinitionUseCase.execute(new CreateReportDefinitionCommand(
                input.getName(),
                input.getDescription(),
                input.getModule(),
                input.getType(),
                input.getDataSource(),
                input.getColumnsJson(),
                input.getFiltersJson(),
                null,
                input.getRequiredPermission(),
                input.getSystemReport() != null && input.getSystemReport()
        ));
        return toPayload(getReportDefinitionByIdUseCase.execute(id));
    }

    @DgsMutation
    public ReportDefinitionPayload updateReportDefinition(@InputArgument("input") UpdateReportDefinitionInput input) {
        updateReportDefinitionUseCase.execute(new UpdateReportDefinitionCommand(
                input.getDefinitionId(),
                input.getName(),
                input.getDescription(),
                input.getColumnsJson(),
                input.getFiltersJson(),
                null
        ));
        return toPayload(getReportDefinitionByIdUseCase.execute(ReportDefinitionId.of(input.getDefinitionId())));
    }

    @DgsMutation
    public ReportResultPayload runReport(@InputArgument("input") RunReportInput input) {
        Map<String, Object> params = Map.of();
        if (input.getParameters() != null && !input.getParameters().isBlank()) {
            try {
                params = objectMapper.readValue(input.getParameters(), new TypeReference<>() {});
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid parameters JSON", e);
            }
        }
        RunReportResult result = runReportUseCase.execute(new RunReportCommand(
                input.getReportDefinitionId(),
                params,
                input.getFormat(),
                input.getPage() != null ? input.getPage() : 0,
                input.getSize() != null ? input.getSize() : 100
        ));

        List<String> rowsJson = result.rows().stream().map(row -> {
            try {
                return objectMapper.writeValueAsString(row);
            } catch (Exception e) {
                log.warn("Failed to serialize report row", e);
                return "{}";
            }
        }).toList();

        String exportData = result.exportData() != null ? Base64.getEncoder().encodeToString(result.exportData()) : null;

        return new ReportResultPayload(
                result.columns(),
                rowsJson,
                result.totalCount(),
                exportData,
                result.exportFormat()
        );
    }

    @DgsMutation
    public SavedReportPayload createSavedReport(@InputArgument("input") CreateSavedReportInput input) {
        var id = createSavedReportUseCase.execute(new CreateSavedReportCommand(
                input.getReportDefinitionId(),
                input.getName(),
                input.getSavedFiltersJson(),
                input.getSavedSortsJson(),
                input.getShared() != null && input.getShared()
        ));
        return new SavedReportPayload(
                id.toString(),
                input.getReportDefinitionId(),
                input.getName(),
                input.getSavedFiltersJson(),
                input.getSavedSortsJson(),
                input.getShared() != null && input.getShared(),
                null,
                null
        );
    }

    @DgsMutation
    public Boolean deleteSavedReport(@InputArgument("id") String id) {
        deleteSavedReportUseCase.executeDelete(id);
        return true;
    }

    @DgsMutation
    public ScheduledReportPayload createScheduledReport(@InputArgument("input") CreateScheduledReportInput input) {
        var id = createScheduledReportUseCase.execute(new CreateScheduledReportCommand(
                input.getReportDefinitionId(),
                input.getName(),
                input.getCronExpression(),
                input.getFormat(),
                input.getRecipientEmails()
        ));
        return new ScheduledReportPayload(
                id.toString(),
                input.getReportDefinitionId(),
                input.getName(),
                input.getCronExpression(),
                input.getFormat(),
                input.getRecipientEmails(),
                true,
                null,
                null,
                null
        );
    }

    @DgsMutation
    public Boolean deleteScheduledReport(@InputArgument("id") String id) {
        deleteScheduledReportUseCase.execute(id);
        return true;
    }
}

