package tr.kontas.erp.crm.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.crm.domain.event.OpportunityCreatedEvent;
import tr.kontas.erp.crm.domain.event.OpportunityLostEvent;
import tr.kontas.erp.crm.domain.event.OpportunityWonEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrmReadModelEventListener {

    private final NamedParameterJdbcTemplate jdbc;

    @EventListener
    @Transactional
    public void handle(OpportunityCreatedEvent event) {
        var params = new MapSqlParameterSource()
                .addValue("opportunityId", event.getOpportunityId())
                .addValue("tenantId", event.getTenantId())
                .addValue("companyId", event.getCompanyId())
                .addValue("opportunityNumber", event.getOpportunityNumber())
                .addValue("title", event.getTitle())
                .addValue("stage", "DISCOVERY");

        jdbc.update("""
                INSERT INTO rpt_crm_opportunities (opportunity_id, tenant_id, company_id, opportunity_number, title, stage)
                VALUES (:opportunityId, :tenantId, :companyId, :opportunityNumber, :title, :stage)
                ON CONFLICT (opportunity_id) DO UPDATE SET
                    title = EXCLUDED.title,
                    stage = EXCLUDED.stage
                """, params);
        log.debug("Read model updated for Opportunity created: {}", event.getOpportunityId());
    }

    @EventListener
    @Transactional
    public void handle(OpportunityWonEvent event) {
        jdbc.update(
                "UPDATE rpt_crm_opportunities SET stage = 'CLOSED_WON' WHERE opportunity_id = :opportunityId",
                new MapSqlParameterSource("opportunityId", event.getOpportunityId())
        );
        log.debug("Read model updated for Opportunity won: {}", event.getOpportunityId());
    }

    @EventListener
    @Transactional
    public void handle(OpportunityLostEvent event) {
        jdbc.update(
                "UPDATE rpt_crm_opportunities SET stage = 'CLOSED_LOST' WHERE opportunity_id = :opportunityId",
                new MapSqlParameterSource("opportunityId", event.getOpportunityId())
        );
        log.debug("Read model updated for Opportunity lost: {}", event.getOpportunityId());
    }
}

