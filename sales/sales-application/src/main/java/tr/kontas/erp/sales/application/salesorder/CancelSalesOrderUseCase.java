package tr.kontas.erp.sales.application.salesorder;

public interface CancelSalesOrderUseCase {
    void cancel(String orderId, String reason);
}
