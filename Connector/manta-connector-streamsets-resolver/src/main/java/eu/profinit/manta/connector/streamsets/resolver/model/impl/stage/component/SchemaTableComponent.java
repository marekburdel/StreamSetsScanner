package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.ISchemaTableComponent;

/**
 *
 * @author mburdel
 */
public class SchemaTableComponent implements ISchemaTableComponent {

    private String schemaName;

    private String tableName;

    private String excludePattern;

    public SchemaTableComponent(String schemaName, String tableName, String excludePattern) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.excludePattern = excludePattern;
    }

    @Override public String getSchemaName() {
        return schemaName;
    }

    @Override public String getTableName() {
        return tableName;
    }

    @Override public String getExcludePattern() {
        return excludePattern;
    }
}
