package tr.kontas.erp.core.platform.multitenancy;

import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class DynamicSchemaNamingStrategy implements PhysicalNamingStrategy, Serializable {

    private final Environment environment;

    private static final Pattern PLACEHOLDER =
            Pattern.compile("\\$\\{([^}]+)}");

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        if (name == null) return null;

        String resolved = resolvePlaceholders(name.getText());
        return Identifier.toIdentifier(resolved, name.isQuoted());
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    private String resolvePlaceholders(String text) {
        Matcher matcher = PLACEHOLDER.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {

            String placeholderContent = matcher.group(1); // key veya key:default

            String key;
            String defaultValue = null;

            int colonIndex = placeholderContent.indexOf(':');

            if (colonIndex != -1) {
                key = placeholderContent.substring(0, colonIndex);
                defaultValue = placeholderContent.substring(colonIndex + 1);
            } else {
                key = placeholderContent;
            }

            String value = environment.getProperty(key);

            if (value == null) {
                value = defaultValue;
            }

            if (value == null) {
                throw new IllegalArgumentException("Unresolved placeholder: " + key);
            }

            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }

        matcher.appendTail(sb);

        return sb.toString();
    }
}