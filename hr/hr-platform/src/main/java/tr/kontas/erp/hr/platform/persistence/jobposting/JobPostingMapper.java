package tr.kontas.erp.hr.platform.persistence.jobposting;

import com.fasterxml.jackson.core.type.TypeReference;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.configuration.JacksonProvider;
import tr.kontas.erp.hr.domain.employee.EmploymentType;
import tr.kontas.erp.hr.domain.jobposting.*;

import java.util.List;

public class JobPostingMapper {

    public static JobPostingJpaEntity toEntity(JobPosting jp) {
        JobPostingJpaEntity e = new JobPostingJpaEntity();
        e.setId(jp.getId().asUUID());
        e.setTenantId(jp.getTenantId().asUUID());
        e.setCompanyId(jp.getCompanyId().asUUID());
        e.setPostingNumber(jp.getPostingNumber().getValue());
        e.setPositionId(jp.getPositionId());
        e.setTitle(jp.getTitle());
        e.setDescription(jp.getDescription());
        e.setRequirementsJson(JacksonProvider.serialize(jp.getRequirements()));
        e.setEmploymentType(jp.getEmploymentType().name());
        e.setStatus(jp.getStatus().name());
        e.setPublishedAt(jp.getPublishedAt());
        e.setClosingDate(jp.getClosingDate());
        return e;
    }

    public static JobPosting toDomain(JobPostingJpaEntity e) {
        List<JobRequirement> reqs = e.getRequirementsJson() != null
                ? JacksonProvider.deserialize(e.getRequirementsJson(), new TypeReference<>() {}) : List.of();
        return new JobPosting(
                JobPostingId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                new JobPostingNumber(e.getPostingNumber()), e.getPositionId(),
                e.getTitle(), e.getDescription(), reqs,
                EmploymentType.valueOf(e.getEmploymentType()),
                JobPostingStatus.valueOf(e.getStatus()), e.getPublishedAt(), e.getClosingDate());
    }
}
