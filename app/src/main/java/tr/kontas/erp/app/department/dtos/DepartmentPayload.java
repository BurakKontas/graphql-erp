package tr.kontas.erp.app.department.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentPayload {
    private String id;
    private String name;
    private String parentId;
    private String companyId;
}
