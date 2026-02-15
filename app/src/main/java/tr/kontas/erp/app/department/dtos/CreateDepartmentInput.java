package tr.kontas.erp.app.department.dtos;

import lombok.Data;

@Data
public class CreateDepartmentInput {
    private String name;
    private String companyId;
    private String parentId; // nullable
}

