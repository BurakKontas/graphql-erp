package tr.kontas.erp.finance.application.creditnote;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.finance.domain.creditnote.CreditNote;
import java.util.List;

public interface GetCreditNotesByCompanyUseCase {
    List<CreditNote> execute(CompanyId companyId);
}

