package tr.kontas.erp.hr.domain.payrollconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DeductionRule {
    private final DeductionType type;
    private final BigDecimal rate;
    private final BigDecimal cap;
    private final boolean employeeSide;
}
