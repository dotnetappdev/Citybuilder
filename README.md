# City Builder - 2D Isometric City Building Game

A Java-based 2D isometric city builder game inspired by SimCity, featuring terrain management, building construction, infrastructure systems, progression mechanics, traffic simulation, and modern game-friendly UI with RDR2-style radial menu.

## Features

### Core Gameplay
- **2D Isometric View**: Classic isometric perspective with smooth rendering
- **Rotatable Camera**: Rotate the view 360° (Q/E keys or menu)
- **Pan & Zoom**: Navigate around your city with mouse drag or WASD keys
- **RDR2-Style Radial Menu**: Right-click for quick access to tools and buildings

### Progression & Economy
- **Money Earning System**: Buildings generate monthly income
  - Commercial buildings (restaurants, shops, petrol stations) produce profit
  - Residential buildings generate tax revenue
  - Service buildings have maintenance costs
- **Monthly Budget**: Track income vs expenses every game month (3 seconds)
- **Starting Capital**: $50,000 to begin your city
- **Economic Balance**: Manage city finances to grow sustainably

### Residents & Happiness
- **Living Residents**: Population grows based on available housing
- **Mood System**: 5 mood states (😊 Happy, 🙂 Content, 😐 Neutral, 😟 Unhappy, 😡 Angry)
- **Happiness Factors**:
  - Access to electricity and water
  - Proximity to services (schools, hospitals, parks)
- **City Happiness**: Average happiness displayed in real-time

### Traffic & Transportation
- **NPC Vehicles**: 6 vehicle types (cars, trucks, buses, taxis, police, ambulances)
- **Automatic Traffic**: Vehicles spawn and drive on roads
- **Traffic Light System**: Red/yellow/green cycles control intersections
- **Realistic Behavior**: Vehicles stop at red lights and navigate roads

### Terrain & Environment
- **Terrain Height**: Raise and lower terrain to create hills and valleys
- **Natural Features**: Rivers, trees, and varied landscapes
- **Multiple Terrain Types**: Grass, water, dirt, and sand

### Building & Construction
- **Diverse Buildings**:
  - **Residential**: Houses ($500), Apartments ($1,200)
  - **Commercial**: Restaurants ($1,500), Fast Food ($900), Shops ($800), Malls ($4,000), Petrol Stations ($2,000), Cinemas ($2,200), Gyms ($1,800)
  - **Services**: Police Stations ($2,000), Fire Stations ($1,800), Schools ($2,500), Hospitals ($3,000), Libraries ($2,000)
  - **Industrial**: Factories ($3,500), Offices ($2,800)
  - **Government**: Town Hall ($5,000)
  - **Recreation**: Parks ($300)
  - **Infrastructure**: Roads ($50), Roundabouts ($200), Traffic Lights ($150)
  - **Utilities**: Power Plants ($4,000), Water Towers ($2,500)

### Zoning System
- Residential Zones (green)
- Commercial Zones (blue)
- Industrial Zones (yellow)
- Zone-based city planning like SimCity

### Infrastructure Systems
- **Electricity Grid**: Power plants distribute electricity within range (20 tiles)
- **Water Supply**: Water towers and natural water sources provide water (15 tiles)
- Buildings require utilities to function properly and keep residents happy

### Tools & Controls
- **RDR2-Style Radial Menu**: Right-click for quick tool/building access
- **Build Tool**: Place buildings and structures
- **Demolish Tool**: Remove unwanted buildings and clear land
- **Terrain Tools**: Raise and lower terrain height
- **Zone Tools**: Designate residential, commercial, and industrial zones

### Modern UI
- **Main Menu**: Clean, modern menu with New Game, Load Game, and Exit options
- **Radial Menu**: RDR2-style quick access menu (right-click)
- **Toolbox Panel**: Organized building and tool selection with prices by category
- **Enhanced Info Panel**: Real-time display of:
  - 💰 Money
  - 👥 Population
  - 😊 City Happiness percentage
  - 📊 Monthly Net Income (profit/loss)
  - 📅 Game Month counter
- **Menu Bar**: Quick access to save/load and view controls
- **Game-Friendly Design**: Emoji icons and color-coded information

### Save & Load System
- **Save Games**: Save your city progress with custom names
- **Load Games**: Resume from any saved game
- **JSON Format**: Human-readable save files stored in `saves/` directory

## Controls

### Mouse Controls
- **Left Click**: Place building/use selected tool
- **Right Click**: Open radial menu (RDR2-style)
- **Middle Click + Drag**: Pan camera
- **Ctrl + Left Click + Drag**: Pan camera

### Keyboard Controls
- **Q**: Rotate camera counter-clockwise
- **E**: Rotate camera clockwise
- **W/A/S/D**: Pan camera up/left/down/right

## Gameplay Loop

1. **Start**: Begin with $50,000 and an empty map
2. **Infrastructure**: Build roads and place power plants & water towers
3. **Residential**: Add houses and apartments for residents
4. **Commercial**: Build restaurants, shops, petrol stations for income
5. **Services**: Add schools, hospitals, police stations for happiness
6. **Traffic**: Place traffic lights at busy intersections
7. **Growth**: Watch population grow and vehicles appear on roads
8. **Economy**: Monitor monthly income vs expenses
9. **Happiness**: Keep residents happy with utilities and services
10. **Expand**: Continue growing your profitable, happy city!

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