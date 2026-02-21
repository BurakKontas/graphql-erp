package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendancePayload {
    private String id;
    private String companyId;
    private String employeeId;
    private String date;
    private String source;
    private String checkIn;
    private String checkOut;
    private String status;
    private int regularMinutes;
    private int overtimeMinutes;
    private String deviceId;
    private String notes;
}
