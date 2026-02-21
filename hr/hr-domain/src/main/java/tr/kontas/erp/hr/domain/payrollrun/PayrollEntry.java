package tr.kontas.erp.hr.domain.payrollrun;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tr.kontas.erp.hr.domain.contract.ComponentType;
import tr.kontas.erp.hr.domain.payrollconfig.DeductionType;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class PayrollEntry {
    private final String employeeId;
    private final String contractId;
    private BigDecimal grossSalary;
    private List<PayrollComponent> components;
    private List<PayrollDeduction> deductions;
    private BigDecimal totalTaxableIncome;
    private BigDecimal incomeTax;
    private BigDecimal employeeSSContribution;
    private BigDecimal employerSSContribution;
    private BigDecimal stampTax;
    private BigDecimal netSalary;
    private BigDecimal totalEmployerCost;
    private int workedDays;
    private int leaveDays;

    @Getter
    @AllArgsConstructor
    public static class PayrollComponent {
        private final ComponentType type;
        private final BigDecimal amount;
        private final boolean taxable;
        private final boolean ssBase;
    }


    @Getter
    @AllArgsConstructor
    public static class PayrollDeduction {
        private final DeductionType type;
        private final BigDecimal amount;
    }
}

