package tr.kontas.erp.reporting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.reporting.application.definition.CreateReportDefinitionCommand;
import tr.kontas.erp.reporting.application.definition.CreateReportDefinitionUseCase;
import tr.kontas.erp.reporting.application.definition.GetReportDefinitionByIdUseCase;
import tr.kontas.erp.reporting.application.definition.GetReportDefinitionsUseCase;
import tr.kontas.erp.reporting.application.definition.UpdateReportDefinitionCommand;
import tr.kontas.erp.reporting.application.definition.UpdateReportDefinitionUseCase;
import tr.kontas.erp.reporting.domain.ReportModule;
import tr.kontas.erp.reporting.domain.ReportType;
import tr.kontas.erp.reporting.domain.definition.ReportDefinition;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportDefinitionService implements
        CreateReportDefinitionUseCase,
        GetReportDefinitionByIdUseCase,
        GetReportDefinitionsUseCase,
        UpdateReportDefinitionUseCase {

    private final ReportDefinitionRepository repository;

    @Override
    @Transactional
    public ReportDefinitionId execute(CreateReportDefinitionCommand command) {
        var tenantId = TenantContext.get();
        var id = ReportDefinitionId.newId();
        var definition = new ReportDefinition(
                id,
                tenantId,
                command.name(),
                command.description(),
                ReportModule.valueOf(command.module()),
                ReportType.valueOf(command.type()),
                command.dataSource(),
                command.columnsJson(),
                command.filtersJson(),
                command.sqlQuery(),
                command.requiredPermission(),
                command.systemReport(),
                true,
                null
        );
        repository.save(definition);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDefinition execute(ReportDefinitionId id) {
        var tenantId = TenantContext.get();
        return repository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Report definition not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDefinition> execute() {
        var tenantId = TenantContext.get();
        return repository.findByTenantId(tenantId);
    }

    @Override
    @Transactional
    public void execute(UpdateReportDefinitionCommand command) {
        var tenantId = TenantContext.get();
        var definition = repository.findById(ReportDefinitionId.of(command.definitionId()), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Report definition not found: " + command.definitionId()));
        definition.update(command.name(), command.description(), command.columnsJson(), command.filtersJson(), command.sqlQuery());
        repository.save(definition);
    }
}

