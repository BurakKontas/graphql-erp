package tr.kontas.erp.app.hr.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.hr.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.application.attendance.*;
import tr.kontas.erp.hr.application.contract.*;
import tr.kontas.erp.hr.application.employee.CreateEmployeeCommand;
import tr.kontas.erp.hr.application.employee.CreateHrEmployeeUseCase;
import tr.kontas.erp.hr.application.employee.GetHrEmployeeByIdUseCase;
import tr.kontas.erp.hr.application.employee.GetHrEmployeesByCompanyUseCase;
import tr.kontas.erp.hr.application.jobapplication.*;
import tr.kontas.erp.hr.application.jobposting.*;
import tr.kontas.erp.hr.application.leavebalance.*;
import tr.kontas.erp.hr.application.leavepolicy.*;
import tr.kontas.erp.hr.application.leaverequest.*;
import tr.kontas.erp.hr.application.payrollconfig.*;
import tr.kontas.erp.hr.application.payrollrun.*;
import tr.kontas.erp.hr.application.performancecycle.*;
import tr.kontas.erp.hr.application.performancereview.*;
import tr.kontas.erp.hr.application.position.*;
import tr.kontas.erp.hr.domain.attendance.*;
import tr.kontas.erp.hr.domain.contract.*;
import tr.kontas.erp.hr.domain.employee.*;
import tr.kontas.erp.hr.domain.jobapplication.*;
import tr.kontas.erp.hr.domain.jobposting.*;
import tr.kontas.erp.hr.domain.leavebalance.*;
import tr.kontas.erp.hr.domain.leavepolicy.*;
import tr.kontas.erp.hr.domain.leaverequest.*;
import tr.kontas.erp.hr.domain.payrollconfig.*;
import tr.kontas.erp.hr.domain.payrollrun.*;
import tr.kontas.erp.hr.domain.performancecycle.*;
import tr.kontas.erp.hr.domain.performancereview.*;
import tr.kontas.erp.hr.domain.position.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class HrGraphql {

    private final CreatePositionUseCase createPositionUseCase;
    private final GetPositionByIdUseCase getPositionByIdUseCase;
    private final GetPositionsByCompanyUseCase getPositionsByCompanyUseCase;

    private final CreateHrEmployeeUseCase createHrEmployeeUseCase;
    private final GetHrEmployeeByIdUseCase getHrEmployeeByIdUseCase;
    private final GetHrEmployeesByCompanyUseCase getHrEmployeesByCompanyUseCase;

    private final CreateContractUseCase createContractUseCase;
    private final GetContractByIdUseCase getContractByIdUseCase;
    private final GetContractsByCompanyUseCase getContractsByCompanyUseCase;

    private final CreateLeavePolicyUseCase createLeavePolicyUseCase;
    private final GetLeavePolicyByIdUseCase getLeavePolicyByIdUseCase;
    private final GetLeavePolicysByCompanyUseCase getLeavePolicysByCompanyUseCase;

    private final CreateLeaveRequestUseCase createLeaveRequestUseCase;
    private final GetLeaveRequestByIdUseCase getLeaveRequestByIdUseCase;
    private final GetLeaveRequestsByCompanyUseCase getLeaveRequestsByCompanyUseCase;

    private final CreateLeaveBalanceUseCase createLeaveBalanceUseCase;
    private final GetLeaveBalanceByIdUseCase getLeaveBalanceByIdUseCase;
    private final GetLeaveBalancesByCompanyUseCase getLeaveBalancesByCompanyUseCase;

    private final CreateAttendanceUseCase createAttendanceUseCase;
    private final GetAttendanceByIdUseCase getAttendanceByIdUseCase;
    private final GetAttendancesByCompanyUseCase getAttendancesByCompanyUseCase;

    private final CreatePayrollConfigUseCase createPayrollConfigUseCase;
    private final GetPayrollConfigByIdUseCase getPayrollConfigByIdUseCase;
    private final GetPayrollConfigsByCompanyUseCase getPayrollConfigsByCompanyUseCase;

    private final CreatePayrollRunUseCase createPayrollRunUseCase;
    private final GetPayrollRunByIdUseCase getPayrollRunByIdUseCase;
    private final GetPayrollRunsByCompanyUseCase getPayrollRunsByCompanyUseCase;

    private final CreatePerformanceCycleUseCase createPerformanceCycleUseCase;
    private final GetPerformanceCycleByIdUseCase getPerformanceCycleByIdUseCase;
    private final GetPerformanceCyclesByCompanyUseCase getPerformanceCyclesByCompanyUseCase;

    private final CreatePerformanceReviewUseCase createPerformanceReviewUseCase;
    private final GetPerformanceReviewByIdUseCase getPerformanceReviewByIdUseCase;
    private final GetPerformanceReviewsByCompanyUseCase getPerformanceReviewsByCompanyUseCase;

    private final CreateJobPostingUseCase createJobPostingUseCase;
    private final GetJobPostingByIdUseCase getJobPostingByIdUseCase;
    private final GetJobPostingsByCompanyUseCase getJobPostingsByCompanyUseCase;

    private final CreateJobApplicationUseCase createJobApplicationUseCase;
    private final GetJobApplicationByIdUseCase getJobApplicationByIdUseCase;
    private final GetJobApplicationsByCompanyUseCase getJobApplicationsByCompanyUseCase;

    @DgsQuery
    public PositionPayload position(@InputArgument String id) {
        return toPayload(getPositionByIdUseCase.execute(PositionId.of(id)));
    }

    @DgsQuery
    public java.util.List<PositionPayload> positions(@InputArgument String companyId) {
        return getPositionsByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public PositionPayload createPosition(@InputArgument CreatePositionInput input) {
        PositionId id = createPositionUseCase.execute(new CreatePositionCommand(
                CompanyId.of(input.getCompanyId()), input.getCode(), input.getTitle(),
                input.getDepartmentId(), input.getLevel(), input.getSalaryGrade(), input.getHeadcount()));
        return toPayload(getPositionByIdUseCase.execute(id));
    }

    @DgsQuery
    public HrEmployeePayload hrEmployee(@InputArgument String id) {
        return toPayload(getHrEmployeeByIdUseCase.execute(EmployeeId.of(id)));
    }

    @DgsQuery
    public java.util.List<HrEmployeePayload> hrEmployees(@InputArgument String companyId) {
        return getHrEmployeesByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public HrEmployeePayload createHrEmployee(@InputArgument CreateHrEmployeeInput input) {
        EmployeeId id = createHrEmployeeUseCase.execute(new CreateEmployeeCommand(
                CompanyId.of(input.getCompanyId()), input.getUserId(),
                input.getFirstName(), input.getLastName(),
                input.getDateOfBirth() != null ? LocalDate.parse(input.getDateOfBirth()) : null,
                input.getNationalId(), input.getGender(), input.getNationality(),
                input.getPersonalEmail(), input.getWorkEmail(), input.getPhone(),
                input.getAddressLine(), input.getCity(), input.getContactCountryCode(),
                input.getPositionId(), input.getDepartmentId(), input.getManagerId(),
                LocalDate.parse(input.getHireDate()), input.getEmploymentType(), input.getCountryCode()));
        return toPayload(getHrEmployeeByIdUseCase.execute(id));
    }

    @DgsQuery
    public ContractPayload contract(@InputArgument String id) {
        return toPayload(getContractByIdUseCase.execute(ContractId.of(id)));
    }

    @DgsQuery
    public java.util.List<ContractPayload> contracts(@InputArgument String companyId) {
        return getContractsByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public ContractPayload createContract(@InputArgument CreateContractInput input) {
        ContractId id = createContractUseCase.execute(new CreateContractCommand(
                CompanyId.of(input.getCompanyId()), input.getEmployeeId(),
                LocalDate.parse(input.getStartDate()),
                input.getEndDate() != null ? LocalDate.parse(input.getEndDate()) : null,
                input.getContractType(), new BigDecimal(input.getGrossSalary()),
                input.getCurrencyCode(), input.getPayrollConfigId()));
        return toPayload(getContractByIdUseCase.execute(id));
    }

    @DgsQuery
    public LeavePolicyPayload leavePolicy(@InputArgument String id) {
        return toPayload(getLeavePolicyByIdUseCase.execute(LeavePolicyId.of(id)));
    }

    @DgsQuery
    public java.util.List<LeavePolicyPayload> leavePolicies(@InputArgument String companyId) {
        return getLeavePolicysByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public LeavePolicyPayload createLeavePolicy(@InputArgument CreateLeavePolicyInput input) {
        LeavePolicyId id = createLeavePolicyUseCase.execute(new CreateLeavePolicyCommand(
                CompanyId.of(input.getCompanyId()), input.getName(), input.getCountryCode(), null));
        return toPayload(getLeavePolicyByIdUseCase.execute(id));
    }

    @DgsQuery
    public LeaveRequestPayload leaveRequest(@InputArgument String id) {
        return toPayload(getLeaveRequestByIdUseCase.execute(LeaveRequestId.of(id)));
    }

    @DgsQuery
    public java.util.List<LeaveRequestPayload> leaveRequests(@InputArgument String companyId) {
        return getLeaveRequestsByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public LeaveRequestPayload createLeaveRequest(@InputArgument CreateLeaveRequestInput input) {
        LeaveRequestId id = createLeaveRequestUseCase.execute(new CreateLeaveRequestCommand(
                CompanyId.of(input.getCompanyId()), input.getEmployeeId(), input.getLeaveType(),
                LocalDate.parse(input.getStartDate()), LocalDate.parse(input.getEndDate()),
                input.getRequestedDays(), input.getReason(), input.getDocumentRef()));
        return toPayload(getLeaveRequestByIdUseCase.execute(id));
    }

    @DgsQuery
    public LeaveBalancePayload leaveBalance(@InputArgument String id) {
        return toPayload(getLeaveBalanceByIdUseCase.execute(LeaveBalanceId.of(id)));
    }

    @DgsQuery
    public java.util.List<LeaveBalancePayload> leaveBalances(@InputArgument String companyId) {
        return getLeaveBalancesByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public LeaveBalancePayload createLeaveBalance(@InputArgument CreateLeaveBalanceInput input) {
        LeaveBalanceId id = createLeaveBalanceUseCase.execute(new CreateLeaveBalanceCommand(
                CompanyId.of(input.getCompanyId()), input.getEmployeeId(), input.getLeaveType(),
                input.getYear(), input.getEntitlementDays(), input.getCarryoverDays()));
        return toPayload(getLeaveBalanceByIdUseCase.execute(id));
    }

    @DgsQuery
    public AttendancePayload attendance(@InputArgument String id) {
        return toPayload(getAttendanceByIdUseCase.execute(AttendanceId.of(id)));
    }

    @DgsQuery
    public java.util.List<AttendancePayload> attendances(@InputArgument String companyId) {
        return getAttendancesByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public AttendancePayload createAttendance(@InputArgument CreateAttendanceInput input) {
        AttendanceId id = createAttendanceUseCase.execute(new CreateAttendanceCommand(
                CompanyId.of(input.getCompanyId()), input.getEmployeeId(),
                LocalDate.parse(input.getDate()), input.getSource(),
                input.getCheckIn() != null ? LocalTime.parse(input.getCheckIn()) : null,
                input.getCheckOut() != null ? LocalTime.parse(input.getCheckOut()) : null,
                input.getStatus(), input.getDeviceId(), input.getNotes()));
        return toPayload(getAttendanceByIdUseCase.execute(id));
    }

    @DgsQuery
    public PayrollConfigPayload payrollConfig(@InputArgument String id) {
        return toPayload(getPayrollConfigByIdUseCase.execute(PayrollConfigId.of(id)));
    }

    @DgsQuery
    public java.util.List<PayrollConfigPayload> payrollConfigs(@InputArgument String companyId) {
        return getPayrollConfigsByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public PayrollConfigPayload createPayrollConfig(@InputArgument CreatePayrollConfigInput input) {
        PayrollConfigId id = createPayrollConfigUseCase.execute(new CreatePayrollConfigCommand(
                CompanyId.of(input.getCompanyId()), input.getCountryCode(), input.getName(),
                input.getValidYear(),
                input.getMinimumWage() != null ? new BigDecimal(input.getMinimumWage()) : null,
                input.getCurrencyCode()));
        return toPayload(getPayrollConfigByIdUseCase.execute(id));
    }

    @DgsQuery
    public PayrollRunPayload payrollRun(@InputArgument String id) {
        return toPayload(getPayrollRunByIdUseCase.execute(PayrollRunId.of(id)));
    }

    @DgsQuery
    public java.util.List<PayrollRunPayload> payrollRuns(@InputArgument String companyId) {
        return getPayrollRunsByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public PayrollRunPayload createPayrollRun(@InputArgument CreatePayrollRunInput input) {
        PayrollRunId id = createPayrollRunUseCase.execute(new CreatePayrollRunCommand(
                CompanyId.of(input.getCompanyId()), input.getYear(), input.getMonth(),
                input.getPaymentDate() != null ? LocalDate.parse(input.getPaymentDate()) : null,
                input.getPayrollConfigId()));
        return toPayload(getPayrollRunByIdUseCase.execute(id));
    }

    @DgsQuery
    public PerformanceCyclePayload performanceCycle(@InputArgument String id) {
        return toPayload(getPerformanceCycleByIdUseCase.execute(PerformanceCycleId.of(id)));
    }

    @DgsQuery
    public java.util.List<PerformanceCyclePayload> performanceCycles(@InputArgument String companyId) {
        return getPerformanceCyclesByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public PerformanceCyclePayload createPerformanceCycle(@InputArgument CreatePerformanceCycleInput input) {
        PerformanceCycleId id = createPerformanceCycleUseCase.execute(new CreatePerformanceCycleCommand(
                CompanyId.of(input.getCompanyId()), input.getName(),
                LocalDate.parse(input.getStartDate()), LocalDate.parse(input.getEndDate()),
                input.getReviewDeadline() != null ? LocalDate.parse(input.getReviewDeadline()) : null, null));
        return toPayload(getPerformanceCycleByIdUseCase.execute(id));
    }

    @DgsQuery
    public PerformanceReviewPayload performanceReview(@InputArgument String id) {
        return toPayload(getPerformanceReviewByIdUseCase.execute(PerformanceReviewId.of(id)));
    }

    @DgsQuery
    public java.util.List<PerformanceReviewPayload> performanceReviews(@InputArgument String companyId) {
        return getPerformanceReviewsByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public PerformanceReviewPayload createPerformanceReview(@InputArgument CreatePerformanceReviewInput input) {
        PerformanceReviewId id = createPerformanceReviewUseCase.execute(new CreatePerformanceReviewCommand(
                CompanyId.of(input.getCompanyId()), input.getCycleId(), input.getEmployeeId(), input.getReviewerId()));
        return toPayload(getPerformanceReviewByIdUseCase.execute(id));
    }

    @DgsQuery
    public JobPostingPayload jobPosting(@InputArgument String id) {
        return toPayload(getJobPostingByIdUseCase.execute(JobPostingId.of(id)));
    }

    @DgsQuery
    public java.util.List<JobPostingPayload> jobPostings(@InputArgument String companyId) {
        return getJobPostingsByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public JobPostingPayload createJobPosting(@InputArgument CreateJobPostingInput input) {
        JobPostingId id = createJobPostingUseCase.execute(new CreateJobPostingCommand(
                CompanyId.of(input.getCompanyId()), input.getPositionId(), input.getTitle(),
                input.getDescription(), input.getEmploymentType(),
                input.getClosingDate() != null ? LocalDate.parse(input.getClosingDate()) : null, null));
        return toPayload(getJobPostingByIdUseCase.execute(id));
    }

    @DgsQuery
    public JobApplicationPayload jobApplication(@InputArgument String id) {
        return toPayload(getJobApplicationByIdUseCase.execute(JobApplicationId.of(id)));
    }

    @DgsQuery
    public java.util.List<JobApplicationPayload> jobApplications(@InputArgument String companyId) {
        return getJobApplicationsByCompanyUseCase.execute(CompanyId.of(companyId)).stream().map(this::toPayload).toList();
    }

    @DgsMutation
    public JobApplicationPayload createJobApplication(@InputArgument CreateJobApplicationInput input) {
        JobApplicationId id = createJobApplicationUseCase.execute(new CreateJobApplicationCommand(
                CompanyId.of(input.getCompanyId()), input.getJobPostingId(),
                input.getFirstName(), input.getLastName(), input.getEmail(),
                input.getPhone(), input.getCvRef()));
        return toPayload(getJobApplicationByIdUseCase.execute(id));
    }

    @DgsData(parentType = "PositionPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        PositionPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "HrEmployeePayload", field = "company")
    public CompletableFuture<CompanyPayload> hrEmployeeCompany(DgsDataFetchingEnvironment dfe) {
        HrEmployeePayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "ContractPayload", field = "company")
    public CompletableFuture<CompanyPayload> contractCompany(DgsDataFetchingEnvironment dfe) {
        ContractPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "LeavePolicyPayload", field = "company")
    public CompletableFuture<CompanyPayload> leavePolicyCompany(DgsDataFetchingEnvironment dfe) {
        LeavePolicyPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "LeaveRequestPayload", field = "company")
    public CompletableFuture<CompanyPayload> leaveRequestCompany(DgsDataFetchingEnvironment dfe) {
        LeaveRequestPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "PayrollConfigPayload", field = "company")
    public CompletableFuture<CompanyPayload> payrollConfigCompany(DgsDataFetchingEnvironment dfe) {
        PayrollConfigPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "PayrollRunPayload", field = "company")
    public CompletableFuture<CompanyPayload> payrollRunCompany(DgsDataFetchingEnvironment dfe) {
        PayrollRunPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "PerformanceCyclePayload", field = "company")
    public CompletableFuture<CompanyPayload> performanceCycleCompany(DgsDataFetchingEnvironment dfe) {
        PerformanceCyclePayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "PerformanceReviewPayload", field = "company")
    public CompletableFuture<CompanyPayload> performanceReviewCompany(DgsDataFetchingEnvironment dfe) {
        PerformanceReviewPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "JobPostingPayload", field = "company")
    public CompletableFuture<CompanyPayload> jobPostingCompany(DgsDataFetchingEnvironment dfe) {
        JobPostingPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "JobApplicationPayload", field = "company")
    public CompletableFuture<CompanyPayload> jobApplicationCompany(DgsDataFetchingEnvironment dfe) {
        JobApplicationPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        return loader.load(payload.getCompanyId());
    }

    private PositionPayload toPayload(Position p) {
        return new PositionPayload(
                p.getId().asUUID().toString(), p.getCompanyId().asUUID().toString(),
                p.getCode().getValue(), p.getTitle().getValue(), p.getDepartmentId(),
                p.getLevel() != null ? p.getLevel().name() : null,
                p.getSalaryGrade() != null ? p.getSalaryGrade().getValue() : null, p.getHeadcount(), p.getFilledCount(), p.getStatus().name());
    }

    private HrEmployeePayload toPayload(Employee e) {
        return new HrEmployeePayload(
                e.getId().asUUID().toString(), e.getCompanyId().asUUID().toString(),
                e.getEmployeeNumber().getValue(), e.getUserId(),
                e.getPersonalInfo() != null ? e.getPersonalInfo().getFirstName() : null,
                e.getPersonalInfo() != null ? e.getPersonalInfo().getLastName() : null,
                e.getPersonalInfo() != null && e.getPersonalInfo().getDateOfBirth() != null ? e.getPersonalInfo().getDateOfBirth().toString() : null,
                e.getPersonalInfo() != null ? e.getPersonalInfo().getNationalId() : null,
                e.getPersonalInfo() != null && e.getPersonalInfo().getGender() != null ? e.getPersonalInfo().getGender().name() : null,
                e.getPersonalInfo() != null ? e.getPersonalInfo().getNationality() : null,
                e.getContactInfo() != null ? e.getContactInfo().getPersonalEmail() : null,
                e.getContactInfo() != null ? e.getContactInfo().getWorkEmail() : null,
                e.getContactInfo() != null ? e.getContactInfo().getPhone() : null,
                e.getContactInfo() != null ? e.getContactInfo().getAddressLine() : null,
                e.getContactInfo() != null ? e.getContactInfo().getCity() : null,
                e.getContactInfo() != null ? e.getContactInfo().getCountryCode() : null,
                e.getPositionId(), e.getDepartmentId(), e.getManagerId(),
                e.getHireDate() != null ? e.getHireDate().toString() : null,
                e.getTerminationDate() != null ? e.getTerminationDate().toString() : null,
                e.getEmploymentType().name(), e.getStatus().name(), e.getCountryCode());
    }

    private ContractPayload toPayload(Contract c) {
        return new ContractPayload(
                c.getId().asUUID().toString(), c.getCompanyId().asUUID().toString(),
                c.getContractNumber().getValue(), c.getEmployeeId(),
                c.getStartDate() != null ? c.getStartDate().toString() : null,
                c.getEndDate() != null ? c.getEndDate().toString() : null,
                c.getContractType().name(), c.getGrossSalary().toPlainString(),
                c.getCurrencyCode(), c.getPayrollConfigId(), c.getStatus().name());
    }

    private LeavePolicyPayload toPayload(LeavePolicy lp) {
        return new LeavePolicyPayload(
                lp.getId().asUUID().toString(), lp.getCompanyId().asUUID().toString(),
                lp.getName(), lp.getCountryCode(), lp.isActive());
    }

    private LeaveRequestPayload toPayload(LeaveRequest lr) {
        return new LeaveRequestPayload(
                lr.getId().asUUID().toString(), lr.getCompanyId().asUUID().toString(),
                lr.getRequestNumber().getValue(), lr.getEmployeeId(), lr.getApproverId(),
                lr.getLeaveType().name(), lr.getStartDate().toString(), lr.getEndDate().toString(),
                lr.getRequestedDays(), lr.getStatus().name(), lr.getReason(),
                lr.getDocumentRef(), lr.getRejectionReason());
    }

    private LeaveBalancePayload toPayload(LeaveBalance lb) {
        return new LeaveBalancePayload(
                lb.getId().asUUID().toString(), lb.getCompanyId().asUUID().toString(),
                lb.getEmployeeId(), lb.getLeaveType().name(), lb.getYear(),
                lb.getEntitlementDays(), lb.getUsedDays(), lb.getCarryoverDays(),
                lb.getPendingDays(), lb.getRemainingDays());
    }

    private AttendancePayload toPayload(Attendance a) {
        return new AttendancePayload(
                a.getId().asUUID().toString(), a.getCompanyId().asUUID().toString(),
                a.getEmployeeId(), a.getDate().toString(), a.getSource().name(),
                a.getCheckIn() != null ? a.getCheckIn().toString() : null,
                a.getCheckOut() != null ? a.getCheckOut().toString() : null,
                a.getStatus().name(), a.getRegularMinutes(), a.getOvertimeMinutes(),
                a.getDeviceId(), a.getNotes());
    }

    private PayrollConfigPayload toPayload(PayrollConfig pc) {
        return new PayrollConfigPayload(
                pc.getId().asUUID().toString(), pc.getCompanyId().asUUID().toString(),
                pc.getCountryCode(), pc.getName(), pc.getValidYear(),
                pc.getMinimumWage() != null ? pc.getMinimumWage().toPlainString() : null,
                pc.getCurrencyCode(), pc.isActive());
    }

    private PayrollRunPayload toPayload(PayrollRun pr) {
        return new PayrollRunPayload(
                pr.getId().asUUID().toString(), pr.getCompanyId().asUUID().toString(),
                pr.getRunNumber().getValue(), pr.getYear(), pr.getMonth(), pr.getStatus().name(),
                pr.getPaymentDate() != null ? pr.getPaymentDate().toString() : null,
                pr.getPayrollConfigId());
    }

    private PerformanceCyclePayload toPayload(PerformanceCycle pc) {
        return new PerformanceCyclePayload(
                pc.getId().asUUID().toString(), pc.getCompanyId().asUUID().toString(),
                pc.getName(), pc.getStartDate().toString(), pc.getEndDate().toString(),
                pc.getReviewDeadline() != null ? pc.getReviewDeadline().toString() : null,
                pc.getStatus().name());
    }

    private PerformanceReviewPayload toPayload(PerformanceReview pr) {
        return new PerformanceReviewPayload(
                pr.getId().asUUID().toString(), pr.getCompanyId().asUUID().toString(),
                pr.getCycleId(), pr.getEmployeeId(), pr.getReviewerId(),
                pr.getStatus().name(), pr.getOverallRating(),
                pr.getStrengths(), pr.getImprovements(), pr.getComments());
    }

    private JobPostingPayload toPayload(JobPosting jp) {
        return new JobPostingPayload(
                jp.getId().asUUID().toString(), jp.getCompanyId().asUUID().toString(),
                jp.getPostingNumber().getValue(), jp.getPositionId(),
                jp.getTitle(), jp.getDescription(), jp.getEmploymentType().name(),
                jp.getStatus().name(),
                jp.getPublishedAt() != null ? jp.getPublishedAt().toString() : null,
                jp.getClosingDate() != null ? jp.getClosingDate().toString() : null);
    }

    private JobApplicationPayload toPayload(JobApplication ja) {
        return new JobApplicationPayload(
                ja.getId().asUUID().toString(), ja.getCompanyId().asUUID().toString(),
                ja.getJobPostingId(),
                ja.getApplicantInfo() != null ? ja.getApplicantInfo().getFirstName() : null,
                ja.getApplicantInfo() != null ? ja.getApplicantInfo().getLastName() : null,
                ja.getApplicantInfo() != null ? ja.getApplicantInfo().getEmail() : null,
                ja.getApplicantInfo() != null ? ja.getApplicantInfo().getPhone() : null,
                ja.getApplicantInfo() != null ? ja.getApplicantInfo().getCvRef() : null,
                ja.getStatus().name(), ja.getCurrentStageNote(),
                ja.getAppliedAt() != null ? ja.getAppliedAt().toString() : null);
    }
}

