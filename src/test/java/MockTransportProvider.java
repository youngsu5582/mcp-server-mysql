import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerSession;
import io.modelcontextprotocol.spec.McpServerTransport;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import java.util.Map;

/**
 * A simplified mock transport provider for testing purposes.
 */
public class MockTransportProvider implements McpServerTransportProvider {

    private final ObjectMapper objectMapper;
    private McpServerSession session;
    // Single sink to simulate outbound message queuing.
    private final Sinks.Many<McpSchema.JSONRPCMessage> sink = Sinks.many().unicast().onBackpressureBuffer();

    public MockTransportProvider() {
        this(new ObjectMapper());
    }

    public MockTransportProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void setSessionFactory(McpServerSession.Factory sessionFactory) {
        // Create a session using the mock transport.
        this.session = sessionFactory.create(new MockTransport());
    }

    @Override
    public Mono<Void> notifyClients(String method, Map<String, Object> params) {
        if (this.session == null) {
            return Mono.error(new RuntimeException("No session available"));
        }
        return this.session.sendNotification(method, params);
    }

    @Override
    public Mono<Void> closeGracefully() {
        return (this.session != null) ? this.session.closeGracefully() : Mono.empty();
    }

    /**
     * A minimal mock implementation of McpServerTransport.
     */
    private class MockTransport implements McpServerTransport {

        @Override
        public Mono<Void> sendMessage(McpSchema.JSONRPCMessage message) {
            // Simulate message enqueueing.
            if (sink.tryEmitNext(message).isSuccess()) {
                return Mono.empty();
            } else {
                return Mono.error(new RuntimeException("Failed to enqueue message"));
            }
        }

        @Override
        public <T> T unmarshalFrom(Object data, TypeReference<T> typeRef) {
            return objectMapper.convertValue(data, typeRef);
        }

        @Override
        public Mono<Void> closeGracefully() {
            sink.tryEmitComplete();
            return Mono.empty();
        }

        @Override
        public void close() {
            sink.tryEmitComplete();
        }
    }
}
