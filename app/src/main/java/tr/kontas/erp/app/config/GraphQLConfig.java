package tr.kontas.erp.app.config;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.context.annotation.Configuration;

@Configuration
@DgsComponent
public class GraphQLConfig {

    @DgsRuntimeWiring
    public RuntimeWiring.Builder addScalars(RuntimeWiring.Builder builder) {
        return builder.scalar(ExtendedScalars.GraphQLBigDecimal);
    }
}