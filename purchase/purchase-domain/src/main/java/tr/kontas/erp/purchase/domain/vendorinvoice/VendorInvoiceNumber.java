package tr.kontas.erp.purchase.domain.vendorinvoice;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class VendorInvoiceNumber extends ValueObject {

    private final String value;

    public VendorInvoiceNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("VendorInvoiceNumber cannot be empty");
        }
        this.value = value;
    }
}
