package tr.kontas.erp.app.businesspartner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class BusinessPartnerPayload {
    private String id;
    private String code;
    private String name;
    private Set<String> roles;
    private String taxNumber;
    private boolean active;
    private String companyId;
}
