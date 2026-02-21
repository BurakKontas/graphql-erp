package tr.kontas.erp.hr.domain.payrollconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TaxBracket {
    private final BigDecimal lowerBound;
    private final BigDecimal upperBound;
    private final BigDecimal rate;
}
