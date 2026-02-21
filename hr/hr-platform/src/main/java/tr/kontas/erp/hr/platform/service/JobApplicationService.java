package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.jobapplication.*;
import tr.kontas.erp.hr.domain.jobapplication.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobApplicationService implements CreateJobApplicationUseCase, GetJobApplicationByIdUseCase,
        GetJobApplicationsByCompanyUseCase, GetJobApplicationsByIdsUseCase {

    private final JobApplicationRepository jobApplicationRepository;

    @Override
    public JobApplicationId execute(CreateJobApplicationCommand cmd) {
        TenantId tenantId = TenantContext.get();
        JobApplicationId id = JobApplicationId.newId();
        ApplicantInfo ai = new ApplicantInfo(cmd.firstName(), cmd.lastName(), cmd.email(), cmd.phone(), cmd.cvRef());
        JobApplication application = new JobApplication(id, tenantId, cmd.companyId(), cmd.jobPostingId(),
                ai, ApplicationStatus.APPLIED, null, List.of(), LocalDate.now());
        jobApplicationRepository.save(application);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplication execute(JobApplicationId id) {
        return jobApplicationRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("JobApplication not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplication> execute(CompanyId companyId) {
        return jobApplicationRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplication> execute(List<JobApplicationId> ids) {
        return jobApplicationRepository.findByIds(ids);
    }
}
