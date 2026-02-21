package tr.kontas.erp.hr.application.attendance;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateAttendanceCommand(CompanyId companyId, String employeeId, LocalDate date, String source, LocalTime checkIn, LocalTime checkOut, String status, String deviceId, String notes) {}
