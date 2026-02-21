package tr.kontas.erp.purchase.platform.persistence.purchaserequest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_requests")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseRequestJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "request_number", nullable = false, unique = true)
    private String requestNumber;

    @Column(name = "requested_by")
    private String requestedBy;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @Column(name = "needed_by")
    private LocalDate neededBy;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PurchaseRequestLineJpaEntity> lines = new ArrayList<>();
}

