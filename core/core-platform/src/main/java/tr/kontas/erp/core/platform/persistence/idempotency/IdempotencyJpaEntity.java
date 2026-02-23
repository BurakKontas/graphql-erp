package tr.kontas.erp.core.platform.persistence.idempotency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "idempotencies", schema = "ERP_USR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID idempotencyKey;

    @Column(name = "response")
    private String response;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
}
