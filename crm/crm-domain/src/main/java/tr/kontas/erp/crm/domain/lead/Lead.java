package tr.kontas.erp.crm.domain.lead;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.contact.ContactId;
import tr.kontas.erp.crm.domain.event.LeadCreatedEvent;
import tr.kontas.erp.crm.domain.event.LeadConvertedEvent;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class Lead extends AggregateRoot<LeadId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final LeadNumber leadNumber;
    private String title;
    private String contactId;
    private String ownerId;
    private LeadStatus status;
    private LeadSource source;
    private BigDecimal estimatedValue;
    private String disqualificationReason;
    private String notes;
    private LocalDate expectedCloseDate;
    private String opportunityId;

    public Lead(LeadId id, TenantId tenantId, CompanyId companyId,
                LeadNumber leadNumber, String title, String contactId,
                String ownerId, LeadStatus status, LeadSource source,
                BigDecimal estimatedValue, String disqualificationReason,
                String notes, LocalDate expectedCloseDate, String opportunityId) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.leadNumber = leadNumber;
        this.title = title;
        this.contactId = contactId;
        this.ownerId = ownerId;
        this.status = status;
        this.source = source;
        this.estimatedValue = estimatedValue;
        this.disqualificationReason = disqualificationReason;
        this.notes = notes;
        this.expectedCloseDate = expectedCloseDate;
        this.opportunityId = opportunityId;
    }


    public static Lead create(LeadId id, TenantId tenantId, CompanyId companyId,
                              LeadNumber number, String title, String contactId,
                              String ownerId, LeadSource source,
                              BigDecimal estimatedValue, String notes,
                              LocalDate expectedCloseDate) {
        Lead l = new Lead(id, tenantId, companyId, number, title, contactId,
                ownerId, LeadStatus.NEW, source, estimatedValue, null,
                notes, expectedCloseDate, null);
        l.registerEvent(new LeadCreatedEvent(
                id.asUUID(), tenantId.asUUID(), companyId.asUUID(), number.getValue(), title));
        return l;
    }


    public void contact() {
        if (status != LeadStatus.NEW) {
            throw new IllegalStateException("Can only contact NEW leads");
        }
        this.status = LeadStatus.CONTACTED;
    }


    public void qualify() {
        if (status != LeadStatus.CONTACTED) {
            throw new IllegalStateException("Can only qualify CONTACTED leads");
        }
        this.status = LeadStatus.QUALIFIED;
    }


    public void disqualify(String reason) {
        if (status == LeadStatus.CONVERTED || status == LeadStatus.DISQUALIFIED) {
            throw new IllegalStateException("Cannot disqualify lead in status: " + status);
        }
        this.status = LeadStatus.DISQUALIFIED;
        this.disqualificationReason = reason;
    }


    public void convert(String opportunityId) {
        if (status != LeadStatus.QUALIFIED) {
            throw new IllegalStateException("Can only convert QUALIFIED leads");
        }
        this.status = LeadStatus.CONVERTED;
        this.opportunityId = opportunityId;
        registerEvent(new LeadConvertedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                leadNumber.getValue(), opportunityId));
    }


    public void updateTitle(String title) {
        this.title = title;
    }


    public void updateNotes(String notes) {
        this.notes = notes;
    }
}

