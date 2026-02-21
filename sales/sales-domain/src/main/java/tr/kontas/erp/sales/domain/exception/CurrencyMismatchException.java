package tr.kontas.erp.sales.domain.exception;

public class CurrencyMismatchException extends SalesDomainException {

    public CurrencyMismatchException(String orderCurrency, String lineCurrency) {
        super(("Currency mismatch: order currency is '%s' but line price currency is '%s'. " +
                "All lines must use the order currency.")
                .formatted(orderCurrency, lineCurrency));
    }
}