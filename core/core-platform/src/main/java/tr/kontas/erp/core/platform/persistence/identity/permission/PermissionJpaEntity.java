package tr.kontas.erp.core.platform.persistence.identity.permission;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"module", "action"})
        })
@Getter
@Setter
public class PermissionJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String module;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String description;
}
