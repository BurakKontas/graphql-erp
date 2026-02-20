package tr.kontas.erp.app.reference.dtos;

import lombok.Data;

@Data
public class CreateUnitInput {
    private String code;
    private String name;
    private String type;
}
