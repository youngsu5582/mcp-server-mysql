import io.modelcontextprotocol.MockMcpTransport;
import io.modelcontextprotocol.server.McpSyncServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class MysqlMcpServerTest {

    @Test
    @DisplayName("Create Server with MockMcpTransport")
    void create_server() {
        final var server = createServer();
        assertThat(server).isNotNull();
        assertThat(server.getServerInfo()
                .name()).isEqualTo("mysql-mcp-server");
    }

    @Test
    @DisplayName("Check graceful shutdown")
    void graceful_shutdown_case() {
        final var server = createServer();
        assertThatCode(server::closeGracefully).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("If not exist prompt, throw exception")
    void remove_prompt_case() {
        final var server = createServer();
        assertThatCode(() -> server.removePrompt(MysqlMcpPrompt.HandlePromptRegistration()
                .prompt()
                .name())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("If not exist tool, throw exception")
    void remove_tool_case() {
        final var server = createServer();
        assertThatCode(() -> server.removeTool(MysqlMcpTool.QueryExecuteSyncToolRegistration()
                .tool()
                .name())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("If not exist resource, throw exception")
    void remove_resource_case() {
        final var server = createServer();
        assertThatCode(() -> server.removeResource(MysqlMcpResource.AllTableSyncResourceRegistration()
                .resource()
                .uri())).doesNotThrowAnyException();
    }

    /**
     * Use `StdioServerTransport` cause exception(java.lang.RuntimeException: Failed to enqueue message)
     * @since 2025.03.22
     * @author youngsu5582
     */
    private McpSyncServer createServer() {
        return MysqlMcpServer.SyncServer(new MockMcpTransport());
    }
}
