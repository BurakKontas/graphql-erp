package tr.kontas.erp.app.reference.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.app.reference.dtos.CreateUnitInput;
import tr.kontas.erp.app.reference.dtos.UnitPayload;
import tr.kontas.erp.core.application.reference.unit.CreateUnitCommand;
import tr.kontas.erp.core.application.reference.unit.CreateUnitUseCase;
import tr.kontas.erp.core.application.reference.unit.GetUnitByCodeUseCase;
import tr.kontas.erp.core.application.reference.unit.GetUnitsUseCase;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.domain.reference.unit.UnitCode;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class UnitGraphql {

    private final CreateUnitUseCase createUnitUseCase;
    private final GetUnitByCodeUseCase getUnitByCodeUseCase;
    private final GetUnitsUseCase getUnitsUseCase;

    public static UnitPayload toPayload(Unit unit) {
        return new UnitPayload(
                unit.getId().getValue(),
                unit.getName(),
                unit.getType().name(),
                unit.isActive()
        );
    }

    @DgsMutation
    public UnitPayload createUnit(@InputArgument("input") CreateUnitInput input) {
        CreateUnitCommand command = new CreateUnitCommand(
                input.getCode(),
                input.getName(),
                input.getType()
        );

        UnitCode code = createUnitUseCase.execute(command);

        return new UnitPayload(
                code.getValue(),
                input.getName(),
                input.getType(),
                true
        );
    }

    @DgsQuery
    public List<UnitPayload> units() {
        return getUnitsUseCase.execute()
                .stream()
                .map(UnitGraphql::toPayload)
                .toList();
    }

    @DgsQuery
    public UnitPayload unit(@InputArgument("code") String code) {
        UnitCode unitCode = new UnitCode(code);
        return toPayload(getUnitByCodeUseCase.execute(unitCode));
    }
}
