import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlUtil {
    public static String showTables() throws SQLException {
        final List<String> tables = new ArrayList<>();

        final String sql = "SHOW TABLES";
        try (final Connection conn = connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }
        return String.join("\n", tables);
    }

    public static String selectQuery(final String query) throws SQLException {
        try (final Connection conn = connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(query)) {

            final StringBuilder sb = new StringBuilder();
            final ResultSetMetaData meta = rs.getMetaData();
            final int colCount = meta.getColumnCount();

            for (int i = 1; i <= colCount; i++) {
                sb.append(meta.getColumnName(i));
                if (i < colCount) sb.append(",");
            }
            sb.append("\n");

            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    final Object value = rs.getObject(i);
                    sb.append(value == null ? "" : value.toString());
                    if (i < colCount) sb.append(",");
                }
                if (!rs.isLast()) sb.append("\n");
            }

            return sb.toString();
        }
    }

    public static boolean updateQuery(final String query) throws SQLException {
        try (final Connection conn = connect();
             final Statement stmt = conn.createStatement()) {
            return stmt.execute(query);
        }
    }

    public static String readTable(final String table) throws SQLException {
        if (!table.matches("[A-Za-z0-9_]+")) {
            throw new IllegalArgumentException("Invalid table name: " + table);
        }

        final String sql = "SELECT * FROM `" + validateStr(table) + "` LIMIT 100";
        try (final Connection conn = connect();
             final PreparedStatement stmt = conn.prepareStatement(sql);
             final ResultSet rs = stmt.executeQuery()) {

            final List<String> rows = new ArrayList<>();
            final ResultSetMetaData md = rs.getMetaData();
            final int colCount = md.getColumnCount();

            rows.add(parseColumn(md, colCount));
            rows.addAll(parseResult(rs, colCount));
            return String.join("\n", rows);
        }
    }

    private static String parseColumn(final ResultSetMetaData metaData, final int colCount) throws SQLException {
        final StringBuilder header = new StringBuilder();
        for (int i = 1; i <= colCount; i++) {
            header.append(metaData.getColumnName(i));
            if (i < colCount) header.append(",");
        }
        return header.toString();
    }

    private static List<String> parseResult(final ResultSet rs, final int colCount) throws SQLException {
        final List<String> rows = new ArrayList<>();
        while (rs.next()) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= colCount; i++) {
                final Object value = rs.getObject(i);
                sb.append(value == null ? "" : value.toString());
                if (i < colCount) sb.append(",");
            }
            rows.add(sb.toString());
        }
        return rows;
    }

    public static DatabaseMetaData getConnectionMetadata() throws SQLException {
        return connect().getMetaData();
    }

    private static Connection connect() throws SQLException {
        //@formatter:off
        final String host = System.getenv().getOrDefault("MYSQL_HOST", "localhost");
        final int port = Integer.parseInt(System.getenv().getOrDefault("MYSQL_PORT", "3306"));
        //@formatter:on
        final String user = getEnv("MYSQL_USER");
        final String pass = getEnv("MYSQL_PASSWORD");
        final String db = getEnv("MYSQL_DATABASE");
        final String url = String.format("jdbc:mysql://%s:%d/%s", host, port, db);
        return DriverManager.getConnection(url, user, pass);
    }

    private static String getEnv(final String key) {
        final String val = System.getenv(key);
        if (val == null) throw new IllegalStateException(key + " is required");
        return val;
    }

    private static String validateStr(final String str) {
        if (str == null) {
            return null;
        }

        return str.replace(" ", "")
                .replace(";", "")
                .trim();
    }
}
