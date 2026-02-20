package tr.kontas.erp.app.reference.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnitPayload {
    private String code;
    private String name;
    private String type;
    private boolean active;
}
