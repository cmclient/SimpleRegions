# SimpleRegions

SimpleRegions is a lightweight Minecraft plugin for managing protected regions within your server, similar to WorldGuard but with a focus on simplicity and minimal system impact. It can be used both as a standalone plugin or integrated as a dependency in other plugin projects.

## Features

- Define, redefine, delete, and manage protected regions
- Efficient and easy to use with minimal performance impact

## Installation

### Standalone Usage

1. Download `SimpleRegions.jar` from the Releases section.
2. Drop it into your server's `plugins` directory.
3. Restart the server or load the plugin.

### As a Dependency

Include SimpleRegions in your plugin project:

```java
private RegionManager regionManager;

@Override
public void onEnable() {
    getLogger().info("Initializing dependencies...");
    SimpleRegions simpleRegions = new SimpleRegions(this);
    this.regionManager = simpleRegions.getRegionManager();
    // Additional plugin code
}
```

## Commands:
- `/region define <name>` - Define a new region. Must have a selected area.
- `/region redefine <name>` - Redefine the boundaries of an existing region.
- `/region delete <name>` - Delete an existing region.
- `/region list` - List all regions.
- `/region info [name]` - Get detailed information about a region. If no name is provided, it checks the player's current location.
- `/region flag <region> <add/remove> <flag>` - Add or remove flags from a region.
- `/region setpriority <region> <priority>` - Set the priority level of a region.

Permissions:
- `sr.admin` - Grants all administrative rights on regions.

## Support
Create issues for bugs or suggestions on our GitHub repository page.

## Contributing
Contributions are welcome! Fork the repo and submit pull requests with your enhancements.
