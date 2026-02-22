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
public class ReportResultPayload {
    private List<String> columns;
    private List<String> rows;
    private long totalCount;
    private String exportData;
    private String exportFormat;
}

