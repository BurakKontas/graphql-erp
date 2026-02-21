package tr.kontas.erp.sales.application.salesorder;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrder;

import java.util.List;

public interface GetSalesOrdersByCompanyUseCase {
    List<SalesOrder> execute(CompanyId companyId);
}
