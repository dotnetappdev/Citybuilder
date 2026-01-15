# Traffic Flow Control System

## Overview
Players can now control traffic flow on roads with directional arrows, allowing for one-way streets and custom traffic patterns.

## Features

### 7 Traffic Direction Options
1. **North (↑)**: Vehicles can only move upward
2. **South (↓)**: Vehicles can only move downward
3. **East (→)**: Vehicles can only move right
4. **West (←)**: Vehicles can only move left
5. **North-South (↕)**: Two-way vertical traffic
6. **East-West (↔)**: Two-way horizontal traffic
7. **All Directions (✚)**: Default - vehicles can move in any direction

## How to Use

### Setting Traffic Direction
1. **Via Toolbox**: Click "Set Traffic Flow" button in the Roads section
2. **Via Radial Menu**: Right-click and select "Traffic" option (🚦 icon)
3. Click on any road tile to cycle through direction options
4. Visual arrow appears on the road showing current direction

### Visual Feedback
- **Yellow arrows** displayed on roads with semi-transparent overlay
- Arrows show the allowed direction(s) of travel
- Different symbols for each direction type

### Vehicle Behavior
- Vehicles automatically respect traffic direction rules
- Cannot move onto roads with incompatible traffic flow
- Creates realistic one-way street systems

## Use Cases

### City Planning
- **One-Way Streets**: Set specific directions to control traffic flow
- **Traffic Management**: Reduce congestion at intersections
- **Highway Systems**: Create directional expressways
- **Parking Areas**: Control entry and exit flows

### Strategic Gameplay
- Optimize traffic patterns for efficiency
- Prevent traffic jams in busy areas
- Create realistic urban road networks
- Design highway on-ramps and off-ramps

## Technical Details

### Implementation
- Each `Tile` has a `TrafficDirection` property
- Default direction is `ALL_DIRECTIONS`
- Direction persists with save/load system
- Vehicles check `canMoveToTile()` before moving

### Controls
- **Left-click** on road while in traffic mode: Cycle to next direction
- **Cycling order**: North → South → East → West → North-South → East-West → All Directions → (repeat)

## Tips
- Use one-way streets to prevent traffic from backing up
- Create circular traffic patterns around city blocks
- Combine with traffic lights for complete traffic management
- Set main roads to specific directions for highway systems
