package tr.kontas.erp.hr.domain.jobapplication;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class JobApplication extends AggregateRoot<JobApplicationId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String jobPostingId;
    private ApplicantInfo applicantInfo;
    private ApplicationStatus status;
    private String currentStageNote;
    private final List<InterviewRecord> interviews;
    private final LocalDate appliedAt;

    public JobApplication(JobApplicationId id, TenantId tenantId, CompanyId companyId,
                          String jobPostingId, ApplicantInfo applicantInfo,
                          ApplicationStatus status, String currentStageNote,
                          List<InterviewRecord> interviews, LocalDate appliedAt) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.jobPostingId = jobPostingId;
        this.applicantInfo = applicantInfo;
        this.status = status;
        this.currentStageNote = currentStageNote;
        this.interviews = new ArrayList<>(interviews != null ? interviews : List.of());
        this.appliedAt = appliedAt;
    }


    public List<InterviewRecord> getInterviews() {
        return Collections.unmodifiableList(interviews);
    }


    public void moveToScreening() {
        if (status != ApplicationStatus.APPLIED) {
            throw new IllegalStateException("Can only screen APPLIED applications");
        }
        this.status = ApplicationStatus.SCREENING;
    }


    public void moveToInterview() {
        if (status != ApplicationStatus.SCREENING) {
            throw new IllegalStateException("Can only move to interview from SCREENING");
        }
        this.status = ApplicationStatus.INTERVIEW;
    }


    public void addInterview(InterviewRecord record) {
        if (status != ApplicationStatus.INTERVIEW) {
            throw new IllegalStateException("Can only add interviews in INTERVIEW status");
        }
        this.interviews.add(record);
    }


    public void makeOffer() {
        if (status != ApplicationStatus.INTERVIEW) {
            throw new IllegalStateException("Can only make offer from INTERVIEW");
        }
        this.status = ApplicationStatus.OFFER;
    }


    public void hire() {
        if (status != ApplicationStatus.OFFER) {
            throw new IllegalStateException("Can only hire from OFFER");
        }
        this.status = ApplicationStatus.HIRED;
    }


    public void reject(String reason) {
        if (status == ApplicationStatus.HIRED || status == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Cannot reject application in status: " + status);
        }
        this.currentStageNote = reason;
        this.status = ApplicationStatus.REJECTED;
    }
}

