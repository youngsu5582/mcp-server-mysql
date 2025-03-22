import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class MysqlMcpServer {
    private static final Logger logger = LoggerFactory.getLogger(MysqlMcpServer.class);

    public static McpSyncServer SyncServer(final McpServerTransportProvider transport) {

        logger.info("Starting MySQL MCP Server...");
        try {
            final DatabaseMetaData metadata = MysqlUtil.getConnectionMetadata();
            logger.info("Database Config : {} as {}", metadata.getURL(), metadata.getUserName());
        } catch (final SQLException e) {
            logger.error("Database Connect Error : {} ", e.getMessage(), e);
            throw new IllegalStateException(String.format("Database error: %s", e.getMessage()), e);
        }

        final McpSyncServer syncServer = McpServer.sync(transport)
                .serverInfo("mysql-mcp-server", "0.0.1")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .resources(false, true)
                        .tools(true)
                        .prompts(true)
                        .logging()
                        .build())
                .prompts(MysqlMcpPrompt.HandlePromptRegistration())
                .tools(MysqlMcpTool.QueryExecuteSyncToolRegistration())
                .resources(MysqlMcpResource.AllTableSyncResourceRegistration())
                .build();

        syncServer.loggingNotification(McpSchema.LoggingMessageNotification.builder()
                .level(McpSchema.LoggingLevel.INFO)
                .logger("mysql-mcp-logger")
                .data("Server initialized")
                .build());
        logger.info("MySQL MCP Server Started");
        return syncServer;
    }
}
