package tr.kontas.erp.app.employee.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeePayload {
    private String id;
    private String name;
    private String departmentId;
}
