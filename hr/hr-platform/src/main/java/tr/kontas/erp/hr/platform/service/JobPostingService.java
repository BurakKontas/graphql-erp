package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.jobposting.*;
import tr.kontas.erp.hr.application.port.JobPostingNumberGeneratorPort;
import tr.kontas.erp.hr.domain.employee.EmploymentType;
import tr.kontas.erp.hr.domain.jobposting.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobPostingService implements CreateJobPostingUseCase, GetJobPostingByIdUseCase,
        GetJobPostingsByCompanyUseCase, GetJobPostingsByIdsUseCase {

    private final JobPostingRepository jobPostingRepository;
    private final JobPostingNumberGeneratorPort numberGenerator;

    @Override
    public JobPostingId execute(CreateJobPostingCommand cmd) {
        TenantId tenantId = TenantContext.get();
        JobPostingId id = JobPostingId.newId();
        JobPostingNumber number = numberGenerator.generate(tenantId, cmd.companyId(), LocalDate.now().getYear());
        List<JobRequirement> reqs = cmd.requirements() != null ? cmd.requirements().stream()
                .map(r -> new JobRequirement(RequirementType.valueOf(r.type()), r.description(), r.mandatory()))
                .toList() : List.of();
        JobPosting posting = new JobPosting(id, tenantId, cmd.companyId(), number, cmd.positionId(),
                cmd.title(), cmd.description(), reqs, EmploymentType.valueOf(cmd.employmentType()),
                JobPostingStatus.DRAFT, null, cmd.closingDate());
        jobPostingRepository.save(posting);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public JobPosting execute(JobPostingId id) {
        return jobPostingRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("JobPosting not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobPosting> execute(CompanyId companyId) {
        return jobPostingRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobPosting> execute(List<JobPostingId> ids) {
        return jobPostingRepository.findByIds(ids);
    }
}
