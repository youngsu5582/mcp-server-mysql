import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import java.sql.SQLException;
import java.util.List;


public class MysqlMcpTool {
    public static McpServerFeatures.SyncToolSpecification QueryExecuteSyncToolRegistration() {
        return new McpServerFeatures.SyncToolSpecification(executeSqlTool(),
                (exchange, arguments) -> {
                    try {
                        final String query = (String) arguments.get("query");

                        if (query.toUpperCase()
                                .startsWith("SELECT")) {
                            final var result = MysqlUtil.selectQuery(query);
                            return new McpSchema.CallToolResult(List.of(new McpSchema.TextContent(result)), false);
                        } else if (query.toUpperCase()
                                .startsWith("SHOW TABLES")) {
                            final var result = MysqlUtil.showTables();
                            return new McpSchema.CallToolResult(List.of(new McpSchema.TextContent(result)), false);
                        } else {
                            final var result = MysqlUtil.updateQuery(query);
                            return result ? new McpSchema.CallToolResult(List.of(new McpSchema.TextContent("Execute Success")), false) :
                                    new McpSchema.CallToolResult(List.of(new McpSchema.TextContent("Execute False")), false);
                        }
                    } catch (final SQLException e) {
                        return new McpSchema.CallToolResult(List.of(new McpSchema.TextContent(String.format("Error executing query: %s", e))), true);
                    }
                }
        );
    }

    private static McpSchema.Tool executeSqlTool() {
        return new McpSchema.Tool("execute_sql", "Execute an SQL query on the MySQL server",
                """
                        {
                            "type": "object",
                            "properties": {
                                "query": {
                                    "type": "string",
                                    "description": "The SQL query to execute"
                                }
                            },
                            "required": ["query"]
                        }
                        """);
    }
}
