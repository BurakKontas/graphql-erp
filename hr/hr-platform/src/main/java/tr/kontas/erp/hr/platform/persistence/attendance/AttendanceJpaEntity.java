package tr.kontas.erp.hr.platform.persistence.attendance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "attendances")
@Getter
@Setter
@NoArgsConstructor
public class AttendanceJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "check_in")
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "regular_minutes")
    private int regularMinutes;

    @Column(name = "overtime_minutes")
    private int overtimeMinutes;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "notes")
    private String notes;
}
