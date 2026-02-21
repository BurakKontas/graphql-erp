package tr.kontas.erp.hr.domain.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SalaryComponent {
    private final ComponentType type;
    private final BigDecimal amount;
    private final boolean taxable;
    private final boolean socialSecurityBase;
}
