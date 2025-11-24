# KPlotLib Class Diagrams

This directory contains PlantUML class diagrams documenting the kplotlib architecture.

## Available Diagrams

### 1. **class-diagram.puml** - Complete Class Diagram
**Comprehensive view of all library classes**

Shows all main plotting classes with:
- Complete property listings
- All public methods
- Relationships and dependencies
- DSL extension functions

**Best for:** Detailed API reference, understanding full class structure

**Key sections:**
- Plot (main orchestrator)
- Series and PlotStyle (data representation)
- Axis (axis configuration)
- Theme system (5 sub-themes: Font, Color, Margin, Grid, AxisFormat)
- Renderers (Renderer, SVGRenderer)
- AnimatedPlotPanel (animation support)
- AxisFormatter utility
- DSL functions

---

### 2. **class-diagram-simplified.puml** - Simplified Overview
**High-level architecture diagram**

Shows the essential structure without implementation details:
- Core components and their roles
- Primary relationships
- Grouping of related classes

**Best for:** Quick understanding, onboarding new developers, presentations

**Highlights:**
- Plot as central orchestrator
- Data flow from Series to Plot to Renderers
- Theme system aggregation
- Three rendering pathways

---

### 3. **class-diagram-theme.puml** - Theme System
**Deep dive into the theming subsystem**

Detailed view of:
- Theme composition (5 data classes)
- Factory methods for preset themes
- FontTheme, ColorTheme, MarginTheme, GridTheme, AxisFormatTheme
- AxisFormatter utility integration
- All theme presets (Default, Presentation, Paper, Dark, Minimal)

**Best for:** Understanding customization options, extending themes

---

### 4. **class-diagram-rendering.puml** - Rendering System
**Deep dive into the rendering pipeline**

Shows:
- Renderer (Graphics2D/PNG/Screen output)
- SVGRenderer (vector output without dependencies)
- AnimatedPlotPanel (frame-based animation)
- Integration with Java Swing (JFrame, JPanel, Timer)
- Output format relationships
- Detailed rendering pipeline notes

**Best for:** Understanding rendering internals, implementing new renderers

---

## Viewing the Diagrams

### Online Viewers
- [PlantUML Online Server](http://www.plantuml.com/plantuml/uml/)
- [PlantText](https://www.planttext.com/)

### IDE Plugins
- **IntelliJ IDEA:** [PlantUML Integration](https://plugins.jetbrains.com/plugin/7017-plantuml-integration)
- **VS Code:** [PlantUML Extension](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml)
- **Eclipse:** [PlantUML Plugin](https://plantuml.com/eclipse)

### Command Line
```bash
# Install PlantUML (requires Java)
# macOS
brew install plantuml

# Generate PNG images
plantuml docs/class-diagram.puml
plantuml docs/class-diagram-simplified.puml
plantuml docs/class-diagram-theme.puml
plantuml docs/class-diagram-rendering.puml

# Generate SVG images
plantuml -tsvg docs/class-diagram.puml
```

---

## Class Relationship Legend

| Symbol | Meaning |
|--------|---------|
| `*--` | Composition (contains, ownership) |
| `o--` | Aggregation (has, reference) |
| `--\|>` | Inheritance (extends) |
| `..>` | Dependency (uses) |
| `..` | Association (linked) |

---

## Architecture Overview

### Core Design Patterns

1. **Composition over Inheritance**
   - Plot composes Axis, Theme, and Series
   - Theme composes 5 specialized sub-themes

2. **Immutable Configuration**
   - Theme and sub-themes are data classes
   - Series uses mutable lists for animation support

3. **Builder Pattern (DSL)**
   - Kotlin DSL for fluent plot construction
   - Extension functions (plot, line, scatter, animate)

4. **Strategy Pattern**
   - Renderer and SVGRenderer implement different rendering strategies
   - Theme presets implement different styling strategies

5. **Observer Pattern**
   - AnimatedPlotPanel uses callback for frame updates

### Package Structure

```
sml.plotlib.core/
├── Plot.kt              (main orchestrator)
├── Series.kt            (data container)
├── Axis.kt              (axis configuration)
├── PlotStyle.kt         (series styling)
├── Theme.kt             (theme system + AxisFormatter)
├── Renderer.kt          (Graphics2D rendering)
├── SVGRenderer.kt       (SVG rendering)
├── AnimatedPlotPanel.kt (animation)
├── DSL.kt               (convenience functions)
└── Util.kt              (extensions)
```

---

## Key Classes Summary

| Class | Type | Responsibility |
|-------|------|----------------|
| **Plot** | Core | Central orchestrator; manages series, axes, theme |
| **Series** | Data | Data container with optional error regions |
| **Axis** | Config | Axis configuration and tick generation |
| **PlotStyle** | Config | Visual styling for individual series |
| **Theme** | Config | Complete theming system with presets |
| **FontTheme** | Config | Font family, sizes, and styles |
| **ColorTheme** | Config | Colors and palette |
| **MarginTheme** | Config | Margin sizes |
| **GridTheme** | Config | Grid visibility and styling |
| **AxisFormatTheme** | Config | Number formatting options |
| **Renderer** | Render | Graphics2D rendering (PNG/screen) |
| **SVGRenderer** | Render | SVG rendering (no dependencies) |
| **AnimatedPlotPanel** | Animation | Frame-based animation with callbacks |
| **AxisFormatter** | Utility | Number formatting and tick generation |

---

## Dependencies

### External (Java Standard Library)
- `java.awt.*` - Graphics2D, Color, Font, BasicStroke
- `javax.swing.*` - JFrame, JPanel, Timer (for display and animation)
- `java.awt.image.BufferedImage` - PNG output
- `javax.imageio.ImageIO` - Image file writing

### Internal
- No third-party dependencies
- Pure Kotlin/Java implementation
- Self-contained SVG generation

---

## Extension Points

To extend kplotlib:

1. **Custom Themes:** Create new Theme instances with custom sub-themes
2. **Custom Renderers:** Implement new renderer classes that read Plot
3. **Custom Series Types:** Extend Series or create new data containers
4. **Custom Formatters:** Extend AxisFormatter for specialized formatting
5. **DSL Extensions:** Add new extension functions in DSL.kt

