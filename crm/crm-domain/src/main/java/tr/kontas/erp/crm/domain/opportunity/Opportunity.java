package tr.kontas.erp.crm.domain.opportunity;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.event.OpportunityCreatedEvent;
import tr.kontas.erp.crm.domain.event.OpportunityWonEvent;
import tr.kontas.erp.crm.domain.event.OpportunityLostEvent;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class Opportunity extends AggregateRoot<OpportunityId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final OpportunityNumber opportunityNumber;
    private String title;
    private String contactId;
    private String leadId;
    private String ownerId;
    private OpportunityStage stage;
    private BigDecimal probability;
    private BigDecimal expectedValue;
    private String currencyCode;
    private LocalDate expectedCloseDate;
    private String salesOrderId;
    private String lostReason;
    private String notes;

    public Opportunity(OpportunityId id, TenantId tenantId, CompanyId companyId,
                       OpportunityNumber opportunityNumber, String title, String contactId,
                       String leadId, String ownerId, OpportunityStage stage,
                       BigDecimal probability, BigDecimal expectedValue, String currencyCode,
                       LocalDate expectedCloseDate, String salesOrderId,
                       String lostReason, String notes) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.opportunityNumber = opportunityNumber;
        this.title = title;
        this.contactId = contactId;
        this.leadId = leadId;
        this.ownerId = ownerId;
        this.stage = stage;
        this.probability = probability;
        this.expectedValue = expectedValue;
        this.currencyCode = currencyCode;
        this.expectedCloseDate = expectedCloseDate;
        this.salesOrderId = salesOrderId;
        this.lostReason = lostReason;
        this.notes = notes;
    }


    public static Opportunity create(OpportunityId id, TenantId tenantId, CompanyId companyId,
                                     OpportunityNumber number, String title, String contactId,
                                     String leadId, String ownerId, BigDecimal expectedValue,
                                     String currencyCode, LocalDate expectedCloseDate, String notes) {
        Opportunity o = new Opportunity(id, tenantId, companyId, number, title, contactId,
                leadId, ownerId, OpportunityStage.DISCOVERY, BigDecimal.TEN,
                expectedValue, currencyCode, expectedCloseDate, null, null, notes);
        o.registerEvent(new OpportunityCreatedEvent(
                id.asUUID(), tenantId.asUUID(), companyId.asUUID(), number.getValue(), title));
        return o;
    }


    public void moveToStage(OpportunityStage newStage, BigDecimal probability) {
        if (stage == OpportunityStage.CLOSED_WON || stage == OpportunityStage.CLOSED_LOST) {
            throw new IllegalStateException("Cannot change stage of a closed opportunity");
        }
        if (newStage == OpportunityStage.CLOSED_WON || newStage == OpportunityStage.CLOSED_LOST) {
            throw new IllegalStateException("Use win() or lose() to close an opportunity");
        }
        this.stage = newStage;
        this.probability = probability;
    }


    public void win(String salesOrderId) {
        if (stage == OpportunityStage.CLOSED_WON || stage == OpportunityStage.CLOSED_LOST) {
            throw new IllegalStateException("Cannot win an already closed opportunity");
        }
        this.stage = OpportunityStage.CLOSED_WON;
        this.probability = BigDecimal.valueOf(100);
        this.salesOrderId = salesOrderId;
        registerEvent(new OpportunityWonEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                opportunityNumber.getValue(), contactId, salesOrderId));
    }


    public void lose(String reason) {
        if (stage == OpportunityStage.CLOSED_WON || stage == OpportunityStage.CLOSED_LOST) {
            throw new IllegalStateException("Cannot lose an already closed opportunity");
        }
        this.stage = OpportunityStage.CLOSED_LOST;
        this.probability = BigDecimal.ZERO;
        this.lostReason = reason;
        registerEvent(new OpportunityLostEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                opportunityNumber.getValue(), reason));
    }


    public void reopen() {
        if (stage != OpportunityStage.CLOSED_LOST) {
            throw new IllegalStateException("Can only reopen CLOSED_LOST opportunities");
        }
        this.stage = OpportunityStage.NEGOTIATION;
        this.lostReason = null;
        this.probability = BigDecimal.valueOf(30);
    }


    public void updateTitle(String title) {
        this.title = title;
    }


    public void updateNotes(String notes) {
        this.notes = notes;
    }
}

