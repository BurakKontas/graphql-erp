package tr.kontas.erp.hr.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.hr.domain.event.EmployeeCreatedEvent;
import tr.kontas.erp.hr.domain.event.EmployeeTerminatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class HrReadModelEventListener {

    private final NamedParameterJdbcTemplate jdbc;

    @EventListener
    @Transactional
    public void handle(EmployeeCreatedEvent event) {
        var params = new MapSqlParameterSource()
                .addValue("employeeId", event.getEmployeeId())
                .addValue("tenantId", event.getTenantId())
                .addValue("companyId", event.getCompanyId())
                .addValue("employeeNumber", event.getEmployeeNumber())
                .addValue("fullName", event.getFirstName() + " " + event.getLastName())
                .addValue("status", "ACTIVE");

        jdbc.update("""
                INSERT INTO rpt_hr_employees (employee_id, tenant_id, company_id, employee_number, full_name, status)
                VALUES (:employeeId, :tenantId, :companyId, :employeeNumber, :fullName, :status)
                ON CONFLICT (employee_id) DO UPDATE SET
                    full_name = EXCLUDED.full_name,
                    status = EXCLUDED.status
                """, params);
        log.debug("Read model updated for Employee created: {}", event.getEmployeeId());
    }

    @EventListener
    @Transactional
    public void handle(EmployeeTerminatedEvent event) {
        jdbc.update(
                "UPDATE rpt_hr_employees SET status = 'TERMINATED' WHERE employee_id = :employeeId",
                new MapSqlParameterSource("employeeId", event.getEmployeeId())
        );
        log.debug("Read model updated for Employee terminated: {}", event.getEmployeeId());
    }
}

