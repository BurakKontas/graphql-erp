package tr.kontas.erp.app.reference.dtos;

import lombok.Data;

@Data
public class CreateCurrencyInput {
    private String code;
    private String name;
    private String symbol;
    private int fractionDigits;
}
