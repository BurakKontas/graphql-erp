package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RunReportInput {
    private String reportDefinitionId;
    private String parameters;
    private String format;
    private Integer page;
    private Integer size;
}

