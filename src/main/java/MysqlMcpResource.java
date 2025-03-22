import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import java.sql.SQLException;
import java.util.List;

public class MysqlMcpResource {
    public static McpServerFeatures.SyncResourceSpecification AllTableSyncResourceRegistration() {
        return new McpServerFeatures.SyncResourceSpecification(
                new McpSchema.Resource("mysql://{table}/data", "MySQL Table Data", "Contents of a MySQL table", "text/plain", null),
                (exchange, request) -> {
                    final String uri = request.uri();
                    final String table = uri.substring("mysql://".length())
                            .split("/")[0];

                    try {
                        return new McpSchema.ReadResourceResult(
                                List.of(new McpSchema.TextResourceContents(
                                        String.format("mysql://%s/data", table),
                                        "text/plain",
                                        MysqlUtil.readTable(table)
                                )));
                    } catch (final SQLException e) {
                        throw new IllegalStateException(String.format("Database error: %s", e.getMessage()), e);
                    }
                }
        );
    }
}
