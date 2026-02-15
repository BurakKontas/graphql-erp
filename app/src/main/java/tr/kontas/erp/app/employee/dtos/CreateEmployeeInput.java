package tr.kontas.erp.app.employee.dtos;

import lombok.Data;

@Data
public class CreateEmployeeInput {
    private String name;
    private String departmentId;
}


