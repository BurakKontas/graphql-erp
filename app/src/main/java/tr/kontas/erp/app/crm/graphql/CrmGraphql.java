package tr.kontas.erp.app.crm.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.crm.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.crm.application.activity.*;
import tr.kontas.erp.crm.application.contact.*;
import tr.kontas.erp.crm.application.lead.*;
import tr.kontas.erp.crm.application.opportunity.*;
import tr.kontas.erp.crm.application.quote.*;
import tr.kontas.erp.crm.domain.activity.Activity;
import tr.kontas.erp.crm.domain.activity.ActivityId;
import tr.kontas.erp.crm.domain.contact.Contact;
import tr.kontas.erp.crm.domain.contact.ContactId;
import tr.kontas.erp.crm.domain.lead.Lead;
import tr.kontas.erp.crm.domain.lead.LeadId;
import tr.kontas.erp.crm.domain.opportunity.Opportunity;
import tr.kontas.erp.crm.domain.opportunity.OpportunityId;
import tr.kontas.erp.crm.domain.quote.Quote;
import tr.kontas.erp.crm.domain.quote.QuoteId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class CrmGraphql {

    private final CreateContactUseCase createContactUseCase;
    private final GetContactByIdUseCase getContactByIdUseCase;
    private final GetContactsByCompanyUseCase getContactsByCompanyUseCase;
    private final CreateLeadUseCase createLeadUseCase;
    private final GetLeadByIdUseCase getLeadByIdUseCase;
    private final GetLeadsByCompanyUseCase getLeadsByCompanyUseCase;
    private final CreateOpportunityUseCase createOpportunityUseCase;
    private final GetOpportunityByIdUseCase getOpportunityByIdUseCase;
    private final GetOpportunitiesByCompanyUseCase getOpportunitiesByCompanyUseCase;
    private final CreateQuoteUseCase createQuoteUseCase;
    private final GetQuoteByIdUseCase getQuoteByIdUseCase;
    private final GetQuotesByCompanyUseCase getQuotesByCompanyUseCase;
    private final CreateActivityUseCase createActivityUseCase;
    private final GetActivityByIdUseCase getActivityByIdUseCase;
    private final GetActivitiesByCompanyUseCase getActivitiesByCompanyUseCase;

    // ─── Contact mappers ───

    public static ContactPayload toPayload(Contact c) {
        return new ContactPayload(
                c.getId().asUUID().toString(),
                c.getCompanyId().asUUID().toString(),
                c.getContactNumber().getValue(),
                c.getContactType().name(),
                c.getFirstName(), c.getLastName(), c.getCompanyName(),
                c.getJobTitle(), c.getEmail(), c.getPhone(), c.getWebsite(),
                c.getAddress(), c.getBusinessPartnerId(), c.getOwnerId(),
                c.getStatus().name(),
                c.getSource() != null ? c.getSource().name() : null,
                c.getNotes()
        );
    }

    public static LeadPayload toPayload(Lead l) {
        return new LeadPayload(
                l.getId().asUUID().toString(),
                l.getCompanyId().asUUID().toString(),
                l.getLeadNumber().getValue(),
                l.getTitle(), l.getContactId(), l.getOwnerId(),
                l.getStatus().name(),
                l.getSource() != null ? l.getSource().name() : null,
                l.getEstimatedValue(), l.getDisqualificationReason(),
                l.getNotes(),
                l.getExpectedCloseDate() != null ? l.getExpectedCloseDate().toString() : null,
                l.getOpportunityId()
        );
    }

    public static OpportunityPayload toPayload(Opportunity o) {
        return new OpportunityPayload(
                o.getId().asUUID().toString(),
                o.getCompanyId().asUUID().toString(),
                o.getOpportunityNumber().getValue(),
                o.getTitle(), o.getContactId(), o.getLeadId(), o.getOwnerId(),
                o.getStage().name(),
                o.getProbability(), o.getExpectedValue(), o.getCurrencyCode(),
                o.getExpectedCloseDate() != null ? o.getExpectedCloseDate().toString() : null,
                o.getSalesOrderId(), o.getLostReason(), o.getNotes()
        );
    }

    public static QuotePayload toPayload(Quote q) {
        List<QuoteLinePayload> lines = q.getLines().stream().map(l -> new QuoteLinePayload(
                l.getId().asUUID().toString(),
                l.getItemId(), l.getItemDescription(), l.getUnitCode(),
                l.getQuantity(), l.getUnitPrice(), l.getDiscountRate(),
                l.getTaxCode(), l.getTaxRate(),
                l.getLineTotal(), l.getTaxAmount(), l.getLineTotalWithTax()
        )).toList();
        return new QuotePayload(
                q.getId().asUUID().toString(),
                q.getCompanyId().asUUID().toString(),
                q.getQuoteNumber().getValue(),
                q.getOpportunityId(), q.getContactId(), q.getOwnerId(),
                q.getQuoteDate() != null ? q.getQuoteDate().toString() : null,
                q.getExpiryDate() != null ? q.getExpiryDate().toString() : null,
                q.getCurrencyCode(), q.getPaymentTermCode(),
                q.getStatus().name(), q.getVersion(), q.getPreviousQuoteId(),
                q.getSubtotal(), q.getTaxTotal(), q.getTotal(),
                q.getDiscountRate(), q.getNotes(), lines
        );
    }

    public static ActivityPayload toPayload(Activity a) {
        return new ActivityPayload(
                a.getId().asUUID().toString(),
                a.getCompanyId().asUUID().toString(),
                a.getActivityType().name(),
                a.getSubject(), a.getOwnerId(),
                a.getStatus().name(),
                a.getRelatedEntityType(), a.getRelatedEntityId(),
                a.getScheduledAt() != null ? a.getScheduledAt().toString() : null,
                a.getCompletedAt() != null ? a.getCompletedAt().toString() : null,
                a.getDurationMinutes(),
                a.getDescription(), a.getOutcome(),
                a.getFollowUpType(),
                a.getFollowUpScheduledAt() != null ? a.getFollowUpScheduledAt().toString() : null,
                a.getFollowUpNote()
        );
    }

    // ─── Contact Queries & Mutations ───

    @DgsQuery
    public ContactPayload crmContact(@InputArgument("id") String id) {
        return toPayload(getContactByIdUseCase.execute(ContactId.of(id)));
    }

    @DgsQuery
    public List<ContactPayload> crmContacts(@InputArgument("companyId") String companyId) {
        return getContactsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(CrmGraphql::toPayload).toList();
    }

    @DgsMutation
    public ContactPayload createContact(@InputArgument("input") CreateContactInput input) {
        CreateContactCommand cmd = new CreateContactCommand(
                CompanyId.of(input.companyId()),
                input.contactType(), input.firstName(), input.lastName(),
                input.companyName(), input.jobTitle(), input.email(),
                input.phone(), input.website(), input.address(),
                input.ownerId(), input.source(), input.notes()
        );
        ContactId id = createContactUseCase.execute(cmd);
        return toPayload(getContactByIdUseCase.execute(id));
    }

    // ─── Lead Queries & Mutations ───

    @DgsQuery
    public LeadPayload lead(@InputArgument("id") String id) {
        return toPayload(getLeadByIdUseCase.execute(LeadId.of(id)));
    }

    @DgsQuery
    public List<LeadPayload> leads(@InputArgument("companyId") String companyId) {
        return getLeadsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(CrmGraphql::toPayload).toList();
    }

    @DgsMutation
    public LeadPayload createLead(@InputArgument("input") CreateLeadInput input) {
        CreateLeadCommand cmd = new CreateLeadCommand(
                CompanyId.of(input.companyId()),
                input.title(), input.contactId(), input.ownerId(),
                input.source(), input.estimatedValue(), input.notes(),
                input.expectedCloseDate() != null ? LocalDate.parse(input.expectedCloseDate()) : null
        );
        LeadId id = createLeadUseCase.execute(cmd);
        return toPayload(getLeadByIdUseCase.execute(id));
    }

    // ─── Opportunity Queries & Mutations ───

    @DgsQuery
    public OpportunityPayload opportunity(@InputArgument("id") String id) {
        return toPayload(getOpportunityByIdUseCase.execute(OpportunityId.of(id)));
    }

    @DgsQuery
    public List<OpportunityPayload> opportunities(@InputArgument("companyId") String companyId) {
        return getOpportunitiesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(CrmGraphql::toPayload).toList();
    }

    @DgsMutation
    public OpportunityPayload createOpportunity(@InputArgument("input") CreateOpportunityInput input) {
        CreateOpportunityCommand cmd = new CreateOpportunityCommand(
                CompanyId.of(input.companyId()),
                input.title(), input.contactId(), input.leadId(),
                input.ownerId(), input.expectedValue(), input.currencyCode(),
                input.expectedCloseDate() != null ? LocalDate.parse(input.expectedCloseDate()) : null,
                input.notes()
        );
        OpportunityId id = createOpportunityUseCase.execute(cmd);
        return toPayload(getOpportunityByIdUseCase.execute(id));
    }

    // ─── Quote Queries & Mutations ───

    @DgsQuery
    public QuotePayload quote(@InputArgument("id") String id) {
        return toPayload(getQuoteByIdUseCase.execute(QuoteId.of(id)));
    }

    @DgsQuery
    public List<QuotePayload> quotes(@InputArgument("companyId") String companyId) {
        return getQuotesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(CrmGraphql::toPayload).toList();
    }

    @DgsMutation
    public QuotePayload createQuote(@InputArgument("input") CreateQuoteInput input) {
        CreateQuoteCommand cmd = new CreateQuoteCommand(
                CompanyId.of(input.companyId()),
                input.opportunityId(), input.contactId(), input.ownerId(),
                LocalDate.parse(input.quoteDate()), LocalDate.parse(input.expiryDate()),
                input.currencyCode(), input.paymentTermCode(),
                input.discountRate(), input.notes()
        );
        QuoteId id = createQuoteUseCase.execute(cmd);
        return toPayload(getQuoteByIdUseCase.execute(id));
    }

    // ─── Activity Queries & Mutations ───

    @DgsQuery
    public ActivityPayload activity(@InputArgument("id") String id) {
        return toPayload(getActivityByIdUseCase.execute(ActivityId.of(id)));
    }

    @DgsQuery
    public List<ActivityPayload> activities(@InputArgument("companyId") String companyId) {
        return getActivitiesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(CrmGraphql::toPayload).toList();
    }

    @DgsMutation
    public ActivityPayload createActivity(@InputArgument("input") CreateActivityInput input) {
        CreateActivityCommand cmd = new CreateActivityCommand(
                CompanyId.of(input.companyId()),
                input.activityType(), input.subject(), input.ownerId(),
                input.relatedEntityType(), input.relatedEntityId(),
                input.scheduledAt() != null ? LocalDateTime.parse(input.scheduledAt()) : null,
                input.description()
        );
        ActivityId id = createActivityUseCase.execute(cmd);
        return toPayload(getActivityByIdUseCase.execute(id));
    }

    // ─── Nested resolvers (company) ───

    @DgsData(parentType = "ContactPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        ContactPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.companyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.companyId());
    }

    @DgsData(parentType = "LeadPayload", field = "company")
    public CompletableFuture<CompanyPayload> leadCompany(DgsDataFetchingEnvironment dfe) {
        LeadPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.companyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.companyId());
    }

    @DgsData(parentType = "LeadPayload", field = "contact")
    public CompletableFuture<ContactPayload> leadContact(DgsDataFetchingEnvironment dfe) {
        LeadPayload payload = dfe.getSource();
        DataLoader<String, ContactPayload> dataLoader = dfe.getDataLoader("crmContactLoader");
        if (dataLoader == null || payload.contactId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.contactId());
    }

    @DgsData(parentType = "OpportunityPayload", field = "company")
    public CompletableFuture<CompanyPayload> opportunityCompany(DgsDataFetchingEnvironment dfe) {
        OpportunityPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.companyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.companyId());
    }

    @DgsData(parentType = "OpportunityPayload", field = "contact")
    public CompletableFuture<ContactPayload> opportunityContact(DgsDataFetchingEnvironment dfe) {
        OpportunityPayload payload = dfe.getSource();
        DataLoader<String, ContactPayload> dataLoader = dfe.getDataLoader("crmContactLoader");
        if (dataLoader == null || payload.contactId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.contactId());
    }

    @DgsData(parentType = "QuotePayload", field = "company")
    public CompletableFuture<CompanyPayload> quoteCompany(DgsDataFetchingEnvironment dfe) {
        QuotePayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.companyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.companyId());
    }

    @DgsData(parentType = "QuotePayload", field = "contact")
    public CompletableFuture<ContactPayload> quoteContact(DgsDataFetchingEnvironment dfe) {
        QuotePayload payload = dfe.getSource();
        DataLoader<String, ContactPayload> dataLoader = dfe.getDataLoader("crmContactLoader");
        if (dataLoader == null || payload.contactId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.contactId());
    }

    @DgsData(parentType = "QuotePayload", field = "opportunity")
    public CompletableFuture<OpportunityPayload> quoteOpportunity(DgsDataFetchingEnvironment dfe) {
        QuotePayload payload = dfe.getSource();
        DataLoader<String, OpportunityPayload> dataLoader = dfe.getDataLoader("opportunityLoader");
        if (dataLoader == null || payload.opportunityId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.opportunityId());
    }

    @DgsData(parentType = "ActivityPayload", field = "company")
    public CompletableFuture<CompanyPayload> activityCompany(DgsDataFetchingEnvironment dfe) {
        ActivityPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.companyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.companyId());
    }
}

