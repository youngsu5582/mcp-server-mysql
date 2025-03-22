import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        final McpServerTransportProvider transport = new StdioServerTransportProvider(new ObjectMapper());
        final McpSyncServer syncServer = MysqlMcpServer.SyncServer(transport);
        log.info("Server Activate - {}", syncServer.getServerInfo());
    }
}
