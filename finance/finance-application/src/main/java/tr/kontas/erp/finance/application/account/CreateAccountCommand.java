package tr.kontas.erp.finance.application.account;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreateAccountCommand(
        CompanyId companyId,
        String code,
        String name,
        String type,
        String nature,
        String parentAccountId,
        boolean systemAccount
) {}

