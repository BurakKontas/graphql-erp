package tr.kontas.erp.app.company.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyPayload {
    private String id;
    private String name;
}
