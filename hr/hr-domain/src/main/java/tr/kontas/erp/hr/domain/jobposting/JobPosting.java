package tr.kontas.erp.hr.domain.jobposting;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.employee.EmploymentType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class JobPosting extends AggregateRoot<JobPostingId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final JobPostingNumber postingNumber;
    private final String positionId;
    private String title;
    private String description;
    private final List<JobRequirement> requirements;
    private EmploymentType employmentType;
    private JobPostingStatus status;
    private LocalDate publishedAt;
    private LocalDate closingDate;

    public JobPosting(JobPostingId id, TenantId tenantId, CompanyId companyId,
                      JobPostingNumber postingNumber, String positionId,
                      String title, String description,
                      List<JobRequirement> requirements, EmploymentType employmentType,
                      JobPostingStatus status, LocalDate publishedAt, LocalDate closingDate) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.postingNumber = postingNumber;
        this.positionId = positionId;
        this.title = title;
        this.description = description;
        this.requirements = new ArrayList<>(requirements != null ? requirements : List.of());
        this.employmentType = employmentType;
        this.status = status;
        this.publishedAt = publishedAt;
        this.closingDate = closingDate;
    }


    public List<JobRequirement> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }


    public void addRequirement(JobRequirement requirement) {
        if (status != JobPostingStatus.DRAFT) {
            throw new IllegalStateException("Can only add requirements to DRAFT postings");
        }
        this.requirements.add(requirement);
    }


    public void publish() {
        if (status != JobPostingStatus.DRAFT) {
            throw new IllegalStateException("Can only publish DRAFT postings");
        }
        this.publishedAt = LocalDate.now();
        this.status = JobPostingStatus.PUBLISHED;
    }


    public void close() {
        if (status != JobPostingStatus.PUBLISHED) {
            throw new IllegalStateException("Can only close PUBLISHED postings");
        }
        this.status = JobPostingStatus.CLOSED;
    }


    public void cancel() {
        if (status == JobPostingStatus.CLOSED || status == JobPostingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel posting in status: " + status);
        }
        this.status = JobPostingStatus.CANCELLED;
    }
}

