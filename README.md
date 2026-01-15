# City Builder - 2D Isometric City Building Game

A Java-based 2D isometric city builder game inspired by SimCity, featuring terrain management, building construction, infrastructure systems, and modern UI.

## Features

### Core Gameplay
- **2D Isometric View**: Classic isometric perspective with smooth rendering
- **Rotatable Camera**: Rotate the view 360° (Q/E keys or menu)
- **Pan & Zoom**: Navigate around your city with mouse drag or WASD keys

### Terrain & Environment
- **Terrain Height**: Raise and lower terrain to create hills and valleys
- **Natural Features**: Rivers, trees, and varied landscapes
- **Multiple Terrain Types**: Grass, water, dirt, and sand

### Building & Construction
- **Diverse Buildings**:
  - **Residential**: Houses, Apartments
  - **Services**: Police Stations, Fire Stations, Schools, Hospitals
  - **Commercial**: Shops, Office Buildings
  - **Industrial**: Factories
  - **Government**: Town Hall
  - **Recreation**: Parks
  - **Infrastructure**: Roads, Roundabouts

### Zoning System
- Residential Zones (green)
- Commercial Zones (blue)
- Industrial Zones (yellow)
- Zone-based city planning like SimCity

### Infrastructure Systems
- **Electricity Grid**: Power plants distribute electricity within range
- **Water Supply**: Water towers and natural water sources provide water distribution
- Buildings require utilities to function properly

### Tools & Controls
- **Build Tool**: Place buildings and structures
- **Demolish Tool**: Remove unwanted buildings and clear land
- **Terrain Tools**: Raise and lower terrain height
- **Zone Tools**: Designate residential, commercial, and industrial zones

### Modern UI
- **Main Menu**: Clean, modern menu with New Game, Load Game, and Exit options
- **Toolbox Panel**: Organized building and tool selection with prices
- **Info Panel**: Real-time display of money and population
- **Menu Bar**: Quick access to save/load and view controls

### Save & Load System
- **Save Games**: Save your city progress with custom names
- **Load Games**: Resume from any saved game
- **JSON Format**: Human-readable save files stored in `saves/` directory

## Controls

### Mouse Controls
- **Left Click**: Place building/use selected tool
- **Middle Click + Drag**: Pan camera
- **Ctrl + Left Click + Drag**: Pan camera

### Keyboard Controls
- **Q**: Rotate camera counter-clockwise
- **E**: Rotate camera clockwise
- **W/A/S/D**: Pan camera up/left/down/right

## Building & Running

### Prerequisites
- Java 11 or higher
- Gradle (included via wrapper)

### Build the Game
```bash
./gradlew build
```

### Run the Game
```bash
./gradlew run
```

Or use the convenience task:
```bash
./gradlew runGame
```

### Create Executable JAR
```bash
./gradlew jar
```

Then run with:
```bash
java -jar build/libs/citybuilder-1.0.0.jar
```

## Project Structure
```
src/main/java/com/citybuilder/
├── Main.java                    # Entry point
├── core/
│   └── GameEngine.java          # Game lifecycle management
├── model/
│   ├── GameState.java           # Main game state
│   ├── CityMap.java             # Map and tile management
│   ├── Tile.java                # Individual tile data
│   ├── Camera.java              # Camera system with rotation
│   ├── Building.java            # Building data
│   ├── BuildingType.java        # Building types enum
│   ├── TerrainType.java         # Terrain types enum
│   ├── ZoneType.java            # Zone types enum
│   └── NaturalFeature.java      # Natural features enum
├── ui/
│   ├── MainMenuFrame.java       # Main menu UI
│   ├── GameFrame.java           # Main game window
│   ├── GamePanel.java           # Game rendering panel
│   ├── ToolboxPanel.java        # Building toolbox UI
│   ├── InfoPanel.java           # Game stats display
│   └── ToolMode.java            # Tool mode enum
├── system/
│   ├── ElectricitySystem.java   # Electricity distribution
│   └── WaterSystem.java         # Water distribution
└── util/
    └── IsometricUtils.java      # Isometric math utilities
```

## Technical Details

### Architecture
- **MVC Pattern**: Separation of model, view, and controller logic
- **Swing GUI**: Java Swing for cross-platform UI
- **Isometric Rendering**: Diamond-shaped tiles with height-based rendering
- **JSON Serialization**: Gson library for save/load functionality

### Key Systems
- **Camera System**: Supports 4-way rotation (0°, 90°, 180°, 270°)
- **Infrastructure Systems**: BFS-based electricity and water distribution
- **Terrain Management**: Height-based terrain with visual height differences
- **Zone Management**: Overlay system for city planning zones

## Future Enhancements
- Population simulation and growth
- Economic system with taxes and revenue
- Traffic simulation
- Pollution and environment management
- Disasters and challenges
- Multiplayer support
- Enhanced graphics and animations
- Sound effects and music

## License
This project is open source and available for educational purposes.

## Credits
Developed as a comprehensive Java city builder game with modern design principles.