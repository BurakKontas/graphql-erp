package tr.kontas.erp.core.domain.businesspartner;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
public class BusinessPartner extends AggregateRoot<BusinessPartnerId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final Set<BusinessPartnerRole> roles = new HashSet<>();
    private final BusinessPartnerCode code;
    private BusinessPartnerName name;
    private BusinessPartnerTaxNumber taxNumber;
    private boolean active;

    public BusinessPartner(
            BusinessPartnerId id,
            TenantId tenantId,
            CompanyId companyId,
            BusinessPartnerCode code,
            BusinessPartnerName name,
            Set<BusinessPartnerRole> roles,
            BusinessPartnerTaxNumber taxNumber
    ) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("TenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("CompanyId cannot be null");
        if (roles == null || roles.isEmpty())
            throw new IllegalArgumentException("At least one role must be defined");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.roles.addAll(roles);
        this.taxNumber = taxNumber;
        this.active = true;

        registerEvent(new BusinessPartnerCreatedEvent(id));
    }

    public BusinessPartner(
            BusinessPartnerId id,
            TenantId tenantId,
            CompanyId companyId,
            BusinessPartnerCode code,
            BusinessPartnerName name,
            Set<BusinessPartnerRole> roles,
            BusinessPartnerTaxNumber taxNumber,
            boolean active
    ) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.roles.addAll(roles);
        this.taxNumber = taxNumber;
        this.active = active;
    }

    public void rename(BusinessPartnerName newName) {
        this.name = newName;
    }

    public void changeTaxNumber(BusinessPartnerTaxNumber taxNumber) {
        this.taxNumber = taxNumber;
    }

    public void addRole(BusinessPartnerRole role) {
        roles.add(role);
    }

    public void removeRole(BusinessPartnerRole role) {
        if (roles.size() == 1) {
            throw new IllegalStateException("BusinessPartner must have at least one role");
        }
        roles.remove(role);
    }

    public void deactivate() {
        this.active = false;
    }

    public Set<BusinessPartnerRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public boolean isCustomer() {
        return roles.contains(BusinessPartnerRole.CUSTOMER);
    }

    public boolean isVendor() {
        return roles.contains(BusinessPartnerRole.VENDOR);
    }
}
