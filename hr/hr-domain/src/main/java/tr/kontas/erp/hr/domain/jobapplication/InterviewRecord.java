package tr.kontas.erp.hr.domain.jobapplication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class InterviewRecord {
    private final LocalDate date;
    private final String interviewerId;
    private final int rating;
    private final String notes;
}
