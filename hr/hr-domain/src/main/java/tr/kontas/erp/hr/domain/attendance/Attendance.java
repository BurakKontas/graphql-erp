package tr.kontas.erp.hr.domain.attendance;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
public class Attendance extends AggregateRoot<AttendanceId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String employeeId;
    private final LocalDate date;
    private AttendanceSource source;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private AttendanceStatus status;
    private int regularMinutes;
    private int overtimeMinutes;
    private String deviceId;
    private String notes;

    public Attendance(AttendanceId id, TenantId tenantId, CompanyId companyId,
                      String employeeId, LocalDate date, AttendanceSource source,
                      LocalTime checkIn, LocalTime checkOut, AttendanceStatus status,
                      int regularMinutes, int overtimeMinutes,
                      String deviceId, String notes) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.employeeId = employeeId;
        this.date = date;
        this.source = source;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.regularMinutes = regularMinutes;
        this.overtimeMinutes = overtimeMinutes;
        this.deviceId = deviceId;
        this.notes = notes;
    }


    public void recordCheckOut(LocalTime checkOut) {
        if (this.checkIn == null) {
            throw new IllegalStateException("Cannot check out without checking in");
        }
        if (checkOut.isBefore(this.checkIn)) {
            throw new IllegalArgumentException("Check-out time cannot be before check-in time");
        }
        this.checkOut = checkOut;
        calculateMinutes();
    }


    public void correct(LocalTime newCheckIn, LocalTime newCheckOut) {
        this.checkIn = newCheckIn;
        this.checkOut = newCheckOut;
        if (newCheckIn != null && newCheckOut != null) {
            calculateMinutes();
        }
    }


    private void calculateMinutes() {
        if (checkIn == null || checkOut == null) return;
        long totalMinutes = ChronoUnit.MINUTES.between(checkIn, checkOut);
        int standardMinutes = 480;
        this.regularMinutes = (int) Math.min(totalMinutes, standardMinutes);
        this.overtimeMinutes = (int) Math.max(0, totalMinutes - standardMinutes);
    }
}

