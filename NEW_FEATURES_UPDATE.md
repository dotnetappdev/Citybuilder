# Latest Features Added (Commit de9f297)

## Currency System
- **6 Currency Options**: USD ($), EUR (€), GBP (£), JPY (¥), CAD (C$), AUD (A$)
- **Currency Selector**: Dropdown in info panel to change currency on-the-fly
- **Formatted Display**: All money values display with proper currency symbols
- **Real-time Updates**: Currency changes immediately affect all money displays

## Day/Night Cycle
- **5 Time Periods**:
  - Dawn (12-6 AM) - 50% brightness
  - Morning (6 AM-12 PM) - 100% brightness
  - Afternoon (12-6 PM) - 100% brightness
  - Evening (6-9 PM) - 70% brightness
  - Night (9 PM-12 AM) - 30% brightness
  
- **Visual Effects**: Dark blue overlay at night with adjustable opacity
- **Time Icons**: ☀️ for day, 🌙 for night in UI
- **Time Advancement**: 1 hour per second (24 hours = 24 seconds)

## Date System
- **Starting Date**: January 1, 2000
- **Full Tracking**: Month/Day/Year progression
- **Display Format**: MM/DD/YYYY with time (12-hour format with AM/PM)
- **Monthly Cycles**: One game month = 3 seconds real-time

## Aging System
- **Resident Aging**: Each resident has birth year and current age
- **Age Categories**:
  - Children: Age 0-17
  - Adults: Age 18-64
  - Elderly: Age 65+
- **Automatic Updates**: Age calculated from current game year
- **Lifecycle**: Residents age naturally as game time progresses

## NPC Behavior (Day/Night)
- **Sleep Patterns**: Residents sleep at night (sleeping flag set)
- **Traffic Reduction**: 33% fewer vehicles spawn at night
- **Mood Effects**: Residents get unhappy if not sleeping at night
- **Dynamic Spawning**: Vehicle spawning adjusts to time of day

## City Budget System
- **Income Categories**:
  - Residential Tax (houses, apartments)
  - Commercial Tax (shops, malls, restaurants)
  - Industrial Tax (factories, offices)
  
- **Expense Categories**:
  - Transportation (roads, traffic lights)
  - Healthcare (hospitals)
  - Education (schools, libraries)
  - Public Safety (police, fire stations)
  - Utilities (power plants, water towers)

- **Budget Tracking**: Detailed breakdown of income/expenses by category
- **Net Income Display**: Green for profit, red for deficit
- **Starting Capital**: $50,000 to begin city development

## Airport Buildings
- **Airport Terminal**: $10,000 - Generates $500/month from tourism
- **Runway**: $5,000 - Required for airport operations
- **Hangar**: $3,000 - Aircraft storage facility
- **Control Tower**: $2,500 - Air traffic control

All airport buildings have $150/month maintenance costs.

## UI Enhancements
- **Currency Dropdown**: Select preferred currency (top-left of info panel)
- **Date Display**: Shows current game date (MM/DD/YYYY)
- **Time Display**: Shows current time with day/night icon
- **Enhanced Formatting**: Currency symbols and thousand separators

## Remaining Features to Implement
- Sound effects for city events
- Proper isometric city tiles and assets
- Visual electric wires connecting buildings to power plants
- Additional SimCity-inspired features
