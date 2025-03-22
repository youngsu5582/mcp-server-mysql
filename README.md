> This project is a Java conversion of a Python codebase from
> a [design-computer/mysql_mcp_server](https://github.com/designcomputer/mysql_mcp_server/tree/main).
> It preserves the core functionality and logic of the original Python implementation, enabling seamless integration into
> Java-based environments.

<br>

This project was created as a learning exercise to explore MCP (Model Context Protocol) concepts.

<br>

I welcome suggestions, code contributions, and any help from the community to further improve the project.

<br>

To see the code differences between version 0.7 and version 0.8 of `io.modelcontextprotocol.sdk:mcp-bom`
<br>
compare the [previous commit](https://github.com/youngsu5582/mcp-server-mysql/tree/c7f36a3cb684d6d744cc4d9e01fd43501efbca31) and the [corresponding commit](https://github.com/youngsu5582/mcp-server-mysql/tree/388aec339d28f1117cae1302afdc933530cffd0c).


# MySQL MCP Server

A Model Context Protocol (MCP) server that enables secure interaction with MySQL databases. This server allows AI
assistants to list tables, read data, and execute SQL queries through a controlled interface, making database
exploration and analysis safer and more structured.

## Features

- List available MySQL tables as resources
- Read table contents
- Execute SQL queries with proper error handling
- Secure database access through environment variables
- Comprehensive logging

## Installation

```bash
pip install mysql-mcp-server
```

## Configuration

Set the following environment variables:

```bash
MYSQL_HOST=localhost     # Database host
MYSQL_PORT=3306         # Optional: Database port (defaults to 3306 if not specified)
MYSQL_USER=your_username
MYSQL_PASSWORD=your_password
MYSQL_DATABASE=your_database
```

## Usage

### With Claude Desktop

Add this to your `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mysql": {
      "command": "uv",
      "args": [
        "--directory",
        "path/to/mysql_mcp_server",
        "run",
        "mysql_mcp_server"
      ],
      "env": {
        "MYSQL_HOST": "localhost",
        "MYSQL_PORT": "3306",
        "MYSQL_USER": "your_username",
        "MYSQL_PASSWORD": "your_password",
        "MYSQL_DATABASE": "your_database"
      }
    }
  }
}
```

### As a standalone server

![intellij-setup-env.png](/static/img/intellij-setup-env.png)

- setup your database setting
- run Main Application

## Security Considerations

- Never commit environment variables or credentials
- Use a database user with minimal required permissions
- Consider implementing query whitelisting for production use
- Monitor and log all database operations

## Security Best Practices

This MCP server requires database access to function. For security:

1. **Create a dedicated MySQL user** with minimal permissions
2. **Never use root credentials** or administrative accounts
3. **Restrict database access** to only necessary operations
4. **Enable logging** for audit purposes
5. **Regular security reviews** of database access

See [MySQL Security Configuration Guide](https://github.com/designcomputer/mysql_mcp_server/blob/main/SECURITY.md) for
detailed instructions on:

- Creating a restricted MySQL user
- Setting appropriate permissions
- Monitoring database access
- Security best practices

⚠️ IMPORTANT: Always follow the principle of least privilege when configuring database access.

## License

MIT License - see LICENSE file for details.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
