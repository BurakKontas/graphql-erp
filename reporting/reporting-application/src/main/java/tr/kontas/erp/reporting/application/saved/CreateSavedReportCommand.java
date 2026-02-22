package tr.kontas.erp.reporting.application.saved;

public record CreateSavedReportCommand(
        String reportDefinitionId,
        String name,
        String savedFiltersJson,
        String savedSortsJson,
        boolean shared
) {}

