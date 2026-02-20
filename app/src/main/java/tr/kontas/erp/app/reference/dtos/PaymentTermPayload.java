package tr.kontas.erp.app.reference.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentTermPayload {
    private String code;
    private String name;
    private int dueDays;
    private boolean active;
    private String companyId;
}
