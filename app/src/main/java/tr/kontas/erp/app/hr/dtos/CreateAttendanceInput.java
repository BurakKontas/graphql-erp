package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreateAttendanceInput {
    private String companyId;
    private String employeeId;
    private String date;
    private String source;
    private String checkIn;
    private String checkOut;
    private String status;
    private String deviceId;
    private String notes;
}
