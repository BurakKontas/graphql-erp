package tr.kontas.erp.app.businesspartner.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class CreateBusinessPartnerInput {
    private String companyId;
    private String code;
    private String name;
    private Set<String> roles;
    private String taxNumber; // nullable
}
