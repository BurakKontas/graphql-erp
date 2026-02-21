package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeavePolicyPayload {
    private String id;
    private String companyId;
    private String name;
    private String countryCode;
    private boolean active;
}
