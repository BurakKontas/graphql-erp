package tr.kontas.erp.core.platform.persistence.reference.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "payment_terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTermJpaEntity {

    @Id
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "due_days", nullable = false)
    private int dueDays;

    @Column(name = "active", nullable = false)
    private boolean active;
}
