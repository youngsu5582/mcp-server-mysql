import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransport;
import io.modelcontextprotocol.spec.ServerMcpTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        final ServerMcpTransport transport = new StdioServerTransport(new ObjectMapper());
        final McpSyncServer syncServer = MysqlMcpServer.SyncServer(transport);
        log.info("Server Activate - {}", syncServer.getServerInfo());
    }
}
