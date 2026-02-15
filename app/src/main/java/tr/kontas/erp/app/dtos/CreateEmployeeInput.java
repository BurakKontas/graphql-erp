package tr.kontas.erp.app.dtos;

import lombok.Data;

@Data
public class CreateEmployeeInput {
    private String name;
    private String departmentId;
}


