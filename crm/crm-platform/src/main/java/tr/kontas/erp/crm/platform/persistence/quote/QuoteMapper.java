package tr.kontas.erp.crm.platform.persistence.quote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.configuration.JacksonProvider;
import tr.kontas.erp.crm.domain.quote.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class QuoteMapper {

    private static final ObjectMapper MAPPER = JacksonProvider.get();

    public static QuoteJpaEntity toEntity(Quote q) {
        QuoteJpaEntity e = new QuoteJpaEntity();
        e.setId(q.getId().asUUID());
        e.setTenantId(q.getTenantId().asUUID());
        e.setCompanyId(q.getCompanyId().asUUID());
        e.setQuoteNumber(q.getQuoteNumber().getValue());
        e.setOpportunityId(q.getOpportunityId());
        e.setContactId(q.getContactId());
        e.setOwnerId(q.getOwnerId());
        e.setQuoteDate(q.getQuoteDate());
        e.setExpiryDate(q.getExpiryDate());
        e.setCurrencyCode(q.getCurrencyCode());
        e.setPaymentTermCode(q.getPaymentTermCode());
        e.setStatus(q.getStatus().name());
        e.setVersion(q.getVersion());
        e.setPreviousQuoteId(q.getPreviousQuoteId());
        e.setSubtotal(q.getSubtotal());
        e.setTaxTotal(q.getTaxTotal());
        e.setTotal(q.getTotal());
        e.setDiscountRate(q.getDiscountRate());
        e.setNotes(q.getNotes());
        try {
            List<Map<String, Object>> lineList = q.getLines().stream().map(l -> {
                Map<String, Object> m = new java.util.LinkedHashMap<>();
                m.put("id", l.getId().asUUID().toString());
                m.put("itemId", l.getItemId());
                m.put("itemDescription", l.getItemDescription());
                m.put("unitCode", l.getUnitCode());
                m.put("quantity", l.getQuantity());
                m.put("unitPrice", l.getUnitPrice());
                m.put("discountRate", l.getDiscountRate());
                m.put("taxCode", l.getTaxCode());
                m.put("taxRate", l.getTaxRate());
                return m;
            }).toList();
            e.setLinesJson(MAPPER.writeValueAsString(lineList));
        } catch (Exception ex) {
            log.warn("Failed to serialize quote lines for quote {}: {}", q.getId(), ex.getMessage());
            e.setLinesJson("[]");
        }
        return e;
    }

    public static Quote toDomain(QuoteJpaEntity e) {
        List<QuoteLine> lines = new ArrayList<>();
        try {
            if (e.getLinesJson() != null && !e.getLinesJson().isBlank()) {
                List<Map<String, Object>> raw = MAPPER.readValue(e.getLinesJson(), new TypeReference<>() {});
                for (Map<String, Object> m : raw) {
                    lines.add(new QuoteLine(
                            QuoteLineId.of((String) m.get("id")),
                            (String) m.get("itemId"),
                            (String) m.get("itemDescription"),
                            (String) m.get("unitCode"),
                            toBigDecimal(m.get("quantity")),
                            toBigDecimal(m.get("unitPrice")),
                            toBigDecimal(m.get("discountRate")),
                            (String) m.get("taxCode"),
                            toBigDecimal(m.get("taxRate"))
                    ));
                }
            }
        } catch (Exception ex) {
            log.warn("Failed to deserialize quote lines: {}", ex.getMessage(), ex);
        }
        return new Quote(
                QuoteId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new QuoteNumber(e.getQuoteNumber()),
                e.getOpportunityId(), e.getContactId(), e.getOwnerId(),
                e.getQuoteDate(), e.getExpiryDate(), e.getCurrencyCode(),
                e.getPaymentTermCode(), QuoteStatus.valueOf(e.getStatus()),
                e.getVersion(), e.getPreviousQuoteId(),
                e.getSubtotal(), e.getTaxTotal(), e.getTotal(),
                e.getDiscountRate(), e.getNotes(), lines
        );
    }

    private static BigDecimal toBigDecimal(Object val) {
        if (val == null) return null;
        if (val instanceof BigDecimal bd) return bd;
        if (val instanceof Number n) return BigDecimal.valueOf(n.doubleValue());
        return new BigDecimal(val.toString());
    }
}

