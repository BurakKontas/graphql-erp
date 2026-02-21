package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SetReorderPointInput {
    private String stockLevelId;
    private BigDecimal reorderPoint;
}
