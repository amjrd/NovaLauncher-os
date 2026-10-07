# Nova Launcher OS for Android

A sleek, fluid, and deeply customizable Android home launcher built with Kotlin and Jetpack Compose. Inspired by Nova Launcher, this project brings customizable desktop grids, adaptive icon shapes, rich widgets, dock personalization, universal search, gesture navigation, and an organized app drawer.

## Features

- **Personalized Desktop & Grids**:
  - Customizable grid dimensions (4×4, 4×5, 5×5, 6×5)
  - Horizontal multi-page pager with animated page indicators
  - Adaptive icon shapes: Circle, Squircle, Rounded Square, Teardrop, and Hexagon
  - Icon size scaling (80% to 130%) and toggleable icon labels
  - Long-press desktop to customize wallpapers, widgets, and shortcuts
  - Folders with live 4-app mini-preview, modal dialogs, and renaming

- **Interactive Home Screen Widgets**:
  - **Clock & Weather Widget**: Real-time digital clock, date, local temperature, condition indicators
  - **Nova Search Bar Widget**: Quick search with instant math calculation and Google/web search integration
  - **Control Center Widget**: Quick toggles for Wi-Fi, Bluetooth, Flashlight/Torch, DND, live battery status, and brightness slider
  - **Now Playing Music Widget**: Interactive track controls (play/pause/skip) with progress bar
  - **Quick Scratchpad Widget**: Persistent sticky notes on the desktop

- **Persistent Dock**:
  - Dock slots with customizable styling (Transparent, Glass blur, Card shadow, Tinted accent)
  - Dedicated central App Drawer launch button

- **Comprehensive App Drawer**:
  - Vertical scrolling apps grid with category filters (All, Favorites, Social, Productivity, Tools, Media, Lifestyle)
  - Quick alphabetical index scrubber (A–Z)
  - Real-time instant search bar
  - Hidden apps management

- **Universal Search Overlay**:
  - Real-time arithmetic solver (e.g. `45 * 12`) directly evaluated in search
  - Instant app filtering and web search redirection
  - Suggested and recent queries

- **Quick Settings & Control Center Shade**:
  - Swipe down gesture or quick access to system toggles, sliders, and notification shade

- **Nova Settings Suite**:
  - Full launcher customization: Look & Feel (Icon shapes, Themes, Accent color palette), Desktop Grid, App Drawer columns, Dock styling, Gestures, and Factory reset

- **Built-in App Ecosystem**:
  - Interactive simulations for native Phone, Messages, Camera, Photos, Calculator, Clock/Stopwatch, Weather, and Notes
  - Safe discovery and launching of physical installed applications via `PackageManager`

## Tech Stack
- Kotlin 2.2
- Jetpack Compose with Material Design 3
- Android SDK 36 (AGP 9.1.1)
- Coroutines & Flow architecture
