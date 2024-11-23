# Shulkr Companion

A companion plugin for [Shulkr](https://github.com/Paylicier/Shulkr) that enables seamless log management and uploading directly from your Minecraft server. This plugin integrates with Shulkr's secure log hosting service to help server administrators easily share and analyze their server logs. It's also compatible with [mclo.gs](https://mclo.gs)!

## Features

- 🚀 **Quick Log Upload**
  - Upload logs directly from in-game or console
  - Instant shareable links
  - Supports multiple log files
  - Automatic error detection and analysis

- 📂 **In-Game Log Management**
  - Browse available log files through GUI
  - Interactive menu interface
  - Quick access to recent logs
  - Direct upload functionality

- 🔄 **Service Compatibility**
  - Works with [Shulkr](https://shulkr.notri1.fr)
  - Compatible with [mclo.gs](https://mclo.gs)
  - Easy service switching via config

- 🎮 **User-Friendly Commands**
  - Simple command structure
  - Tab completion support
  - Intuitive GUI interface
  - Click-to-copy links

## Requirements

- Java 14 or higher
- Minecraft Server 1.16+
- Paper, Purpur, or compatible fork

## Installation

1. Download the latest release from the [releases page](https://github.com/Paylicier/ShulkrCompanion/releases)
2. Place the `.jar` file in your server's `plugins` folder
3. Restart your server
4. (Optional) Configure the instance URL in the config file

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/logs` | `shulkr.upload` | Upload latest.log to configured service |
| `/logs list` | `shulkr.upload` | Open the log management GUI |
| `/logs upload [file]` | `shulkr.upload` | Upload a specific log file |

## Permissions

- `shulkr.upload` - Allows users to upload and manage logs
- `shulkr.*` - Grants all plugin permissions

## Configuration

Default `config.yml`:
```yaml
# The URL of the log hosting service
# Use https://shulkr.notri1.fr/api for Shulkr
# Use https://api.mclo.gs for mclo.gs
instance-url: https://shulkr.notri1.fr/api
```

## Usage Examples

1. **Quick Upload**
```bash
/logs
```
Uploads the latest log file and provides a shareable link.

2. **Specific File Upload**
```bash
/logs upload crash-2024-01-01-1.log
```
Uploads a specific log file from the logs directory.

3. **Browse Logs**
```bash
/logs list
```
Opens an interactive GUI to browse and manage log files.

## Service Configuration

### Using with Shulkr
The default configuration uses Shulkr. No changes needed!

### Using with mclo.gs
To use mclo.gs instead of Shulkr:
1. Open `config.yml`
2. Change the `instance-url` to `https://api.mclo.gs`
3. Reload the plugin or restart your server

## Building from Source

1. Clone the repository:
```bash
git clone https://github.com/Paylicier/ShulkrCompanion.git
cd ShulkrCompanion
```

2. Build using Gradle:
```bash
./gradlew shadowJar
```

The compiled jar will be in the `build/libs` directory.

## Contributing

We welcome contributions! Please feel free to:
- Report bugs
- Suggest features
- Submit pull requests
- Improve documentation

1. Fork the repository
2. Create your feature branch: `git checkout -b feature/AmazingFeature`
3. Commit your changes: `git commit -m 'Add AmazingFeature'`
4. Push to the branch: `git push origin feature/AmazingFeature`
5. Open a Pull Request

## Support

- For bug reports and feature requests, use [GitHub Issues](https://github.com/Paylicier/ShulkrCompanion/issues)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Compatible with [mclo.gs](https://mclo.gs)
- Built for integration with [Shulkr](https://shulkr.notri1.fr)
- Community contributions and feedback

---
Made with ❤️ by [Paylicier](https://github.com/Paylicier)
