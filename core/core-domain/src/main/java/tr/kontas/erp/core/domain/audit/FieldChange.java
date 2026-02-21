package tr.kontas.erp.core.domain.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldChange {

    private final String fieldName;
    private final String oldValue;
    private final String newValue;
    private final String maskLevel;
}

