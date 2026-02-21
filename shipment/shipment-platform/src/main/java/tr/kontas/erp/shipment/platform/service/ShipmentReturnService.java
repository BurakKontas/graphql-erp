package tr.kontas.erp.shipment.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.shipment.application.port.ShipmentReturnNumberGeneratorPort;
import tr.kontas.erp.shipment.application.shipmentreturn.*;
import tr.kontas.erp.shipment.domain.shipmentreturn.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentReturnService implements
        CreateShipmentReturnUseCase,
        GetShipmentReturnByIdUseCase,
        GetShipmentReturnsByCompanyUseCase,
        GetShipmentReturnsByIdsUseCase,
        ReceiveShipmentReturnUseCase,
        CompleteShipmentReturnUseCase,
        CancelShipmentReturnUseCase {

    private final ShipmentReturnRepository shipmentReturnRepository;
    private final ShipmentReturnNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public ShipmentReturnId execute(CreateShipmentReturnCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();

        ShipmentReturnNumber number = numberGenerator.generate(tenantId, companyId, LocalDate.now().getYear());

        List<ShipmentReturnLine> lines = command.lines().stream()
                .map(lc -> new ShipmentReturnLine(
                        ShipmentReturnLineId.newId(),
                        lc.shipmentLineId(),
                        lc.itemId(),
                        lc.itemDescription(),
                        lc.unitCode(),
                        lc.quantity(),
                        lc.lineReason()
                ))
                .toList();

        ShipmentReturnId id = ShipmentReturnId.newId();
        ShipmentReturn sr = new ShipmentReturn(
                id, tenantId, companyId, number,
                command.shipmentId(), command.salesOrderId(), command.warehouseId(),
                new ReturnReason(command.reason()),
                lines
        );

        saveAndPublish(sr);
        return id;
    }

    @Override
    public ShipmentReturn execute(ShipmentReturnId id) {
        TenantId tenantId = TenantContext.get();
        return shipmentReturnRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("ShipmentReturn not found: " + id));
    }

    @Override
    public List<ShipmentReturn> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return shipmentReturnRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<ShipmentReturn> execute(List<ShipmentReturnId> ids) {
        return shipmentReturnRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void receive(String shipmentReturnId) {
        ShipmentReturn sr = loadReturn(shipmentReturnId);
        sr.receive();
        saveAndPublish(sr);
    }

    @Override
    @Transactional
    public void complete(String shipmentReturnId) {
        ShipmentReturn sr = loadReturn(shipmentReturnId);
        sr.complete();
        saveAndPublish(sr);
    }

    @Override
    @Transactional
    public void cancel(String shipmentReturnId) {
        ShipmentReturn sr = loadReturn(shipmentReturnId);
        sr.cancel();
        saveAndPublish(sr);
    }

    private ShipmentReturn loadReturn(String id) {
        TenantId tenantId = TenantContext.get();
        return shipmentReturnRepository.findById(ShipmentReturnId.of(id), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("ShipmentReturn not found: " + id));
    }

    private void saveAndPublish(ShipmentReturn sr) {
        shipmentReturnRepository.save(sr);
        eventPublisher.publishAll(sr.getDomainEvents());
        sr.clearDomainEvents();
    }
}

