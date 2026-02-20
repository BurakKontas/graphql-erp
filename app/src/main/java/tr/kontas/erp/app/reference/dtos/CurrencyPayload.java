package tr.kontas.erp.app.reference.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyPayload {
    private String code;
    private String name;
    private String symbol;
    private int fractionDigits;
    private boolean active;
}
