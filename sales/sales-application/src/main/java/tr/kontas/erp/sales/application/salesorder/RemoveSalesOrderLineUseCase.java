package tr.kontas.erp.sales.application.salesorder;

public interface RemoveSalesOrderLineUseCase {
    void removeLine(String orderId, String lineId);
}
