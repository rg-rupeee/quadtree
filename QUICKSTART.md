# QuadTree Quick Start Guide

## Installation

```xml
<!-- Add to your Maven pom.xml -->
<dependency>
    <groupId>com.rupesh</groupId>
    <artifactId>quadtree-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Basic Usage

### 1. Create a QuadTree

```java
import com.rupesh.quadtree.QuadTree;
import com.rupesh.quadtree.SimpleQuadTree;
import com.rupesh.quadtree.geometry.Point;
import com.rupesh.quadtree.geometry.Rectangle;
import com.rupesh.quadtree.util.Entry;

// Define boundaries (x, y, width, height)
Rectangle bounds = new Rectangle(0, 0, 1000, 1000);

// Create QuadTree with split threshold of 4
QuadTree<String> tree = new SimpleQuadTree<>(4, bounds);
```

### 2. Insert Points

```java
// Insert single point
Entry<String> entry = tree.insert(new Point(100, 200), "Location A");

// Insert multiple points
tree.insert(new Point(150, 250), "Location B");
tree.insert(new Point(300, 400), "Location C");
tree.insert(new Point(500, 600), "Location D");

// Update existing point (automatically replaces)
tree.insert(new Point(100, 200), "Location A - Updated");
```

### 3. Check Point Existence

```java
boolean exists = tree.exists(new Point(100, 200));  // true
boolean missing = tree.exists(new Point(999, 999)); // false
```

### 4. Remove Points

```java
boolean removed = tree.remove(new Point(100, 200));  // true
boolean notFound = tree.remove(new Point(999, 999)); // false
```

### 5. Query by Rectangle

```java
// Find all points in a rectangular region
// Parameters: topLeft point, width, height
List<Entry<String>> results = tree.query(new Point(0, 0), 500, 500);

for (Entry<String> entry : results) {
    Point p = entry.point();
    String data = entry.data();
    System.out.println(data + " at (" + p.x() + ", " + p.y() + ")");
}
```

### 6. Query by Circle (Radius)

```java
// Find all points within radius from center
// Parameters: center point, radius
List<Entry<String>> nearby = tree.query(new Point(250, 250), 100.0);

System.out.println("Found " + nearby.size() + " points within radius");
```

## Advanced Usage

### Global Coordinates (Latitude/Longitude)

```java
import com.rupesh.quadtree.GlobalQuadTree;

// Pre-configured for world coordinates
// Latitude: -90 to +90, Longitude: -180 to +180
QuadTree<String> worldMap = new GlobalQuadTree<>(8);

worldMap.insert(new Point(-74.0060, 40.7128), "New York");
worldMap.insert(new Point(0.1276, 51.5074), "London");
worldMap.insert(new Point(139.6917, 35.6895), "Tokyo");

// Find cities near London (within ~500km)
List<Entry<String>> nearLondon = worldMap.query(new Point(0, 51), 10.0);
```

### Custom Data Types

```java
// Store any type of data
record City(String name, int population) {}

QuadTree<City> cities = new SimpleQuadTree<>(4, bounds);
cities.insert(new Point(100, 200), new City("Springfield", 50000));

// Retrieve
List<Entry<City>> found = cities.query(new Point(100, 200), 10, 10);
for (Entry<City> entry : found) {
    City city = entry.data();
    System.out.println(city.name() + ": " + city.population());
}
```

### Handling Exceptions

```java
import com.rupesh.quadtree.exception.PointOutOfBoundsException;

try {
    tree.insert(new Point(9999, 9999), "Out of bounds");
} catch (PointOutOfBoundsException e) {
    System.err.println("Point outside QuadTree boundaries: " + e.getMessage());
}

try {
    tree.query(new Point(0, 0), -10, 20);  // Invalid width
} catch (IllegalArgumentException e) {
    System.err.println("Invalid query parameters: " + e.getMessage());
}
```

## Common Patterns

### Spatial Indexing for Game Objects

```java
record GameObject(String id, String type) {}

Rectangle gameWorld = new Rectangle(-1000, -1000, 2000, 2000);
QuadTree<GameObject> gameObjects = new SimpleQuadTree<>(8, gameWorld);

// Add objects
gameObjects.insert(new Point(100, 200), new GameObject("player1", "PLAYER"));
gameObjects.insert(new Point(120, 180), new GameObject("enemy1", "ENEMY"));

// Find all objects near player
Point playerPos = new Point(100, 200);
List<Entry<GameObject>> nearby = gameObjects.query(playerPos, 50.0);

// Check for collisions
for (Entry<GameObject> entry : nearby) {
    if (entry.data().type().equals("ENEMY")) {
        System.out.println("Collision detected!");
    }
}
```

### Location-based Services

```java
record Store(String name, String category) {}

QuadTree<Store> stores = new GlobalQuadTree<>(10);

// Index stores
stores.insert(new Point(-122.4194, 37.7749), new Store("Coffee Shop", "CAFE"));
stores.insert(new Point(-122.4084, 37.7849), new Store("Grocery", "FOOD"));

// Find stores near user location
Point userLocation = new Point(-122.4100, 37.7800);
List<Entry<Store>> nearbyStores = stores.query(userLocation, 0.05); // ~5km radius

nearbyStores.forEach(entry -> 
    System.out.println(entry.data().name() + " - " + entry.data().category())
);
```

### Update Point Data

```java
// Method 1: Direct update (removes old, inserts new)
tree.insert(new Point(100, 200), "Updated Data");

// Method 2: Check-then-update
if (tree.exists(new Point(100, 200))) {
    tree.remove(new Point(100, 200));
    tree.insert(new Point(100, 200), "New Data");
}
```

## Performance Tips

### 1. Choose Appropriate Split Threshold

```java
// Small threshold (2-4): Better for dense, clustered data
QuadTree<String> dense = new SimpleQuadTree<>(2, bounds);

// Medium threshold (8-16): Good general purpose
QuadTree<String> balanced = new SimpleQuadTree<>(8, bounds);

// Large threshold (32+): Better for sparse data
QuadTree<String> sparse = new SimpleQuadTree<>(32, bounds);
```

### 2. Efficient Batch Operations

```java
// Insert many points
List<Point> points = generatePoints(1000);
for (Point p : points) {
    tree.insert(p, "Data for " + p);
}

// Automatic tree rebalancing happens on remove
// No manual intervention needed
```

### 3. Optimize Query Regions

```java
// Prefer smaller query regions for better performance
List<Entry<String>> efficient = tree.query(new Point(100, 100), 50, 50);

// Avoid querying entire tree
List<Entry<String>> inefficient = tree.query(new Point(0, 0), 1000, 1000);
```

## Boundary Behavior

### Important: Half-Open Intervals

```java
Rectangle bounds = new Rectangle(0, 0, 100, 100);
QuadTree<String> tree = new SimpleQuadTree<>(4, bounds);

// Points at boundaries
tree.insert(new Point(0, 0), "Top-left corner");      // ✅ Included
tree.insert(new Point(50, 50), "Center");             // ✅ Included
tree.insert(new Point(99.9, 99.9), "Near bottom-right"); // ✅ Included
tree.insert(new Point(100, 100), "Bottom-right");     // ❌ OUT OF BOUNDS

// Boundaries are [0, 100) x [0, 100)
// Left/top edges: inclusive (>=)
// Right/bottom edges: exclusive (<)
```

## Common Errors

### Out of Bounds
```java
// Will throw PointOutOfBoundsException
tree.insert(new Point(-1, 50), "data");  // x < bounds.x
tree.insert(new Point(50, 1001), "data"); // y >= bounds.y + bounds.height
```

### Invalid Parameters
```java
// Will throw IllegalArgumentException
new SimpleQuadTree<>(0, bounds);  // threshold must be >= 1
tree.query(point, -10, 20);       // width must be > 0
tree.query(point, 0.0);           // radius must be > 0
```

## Complete Example

```java
import com.rupesh.quadtree.*;
import com.rupesh.quadtree.geometry.*;
import com.rupesh.quadtree.util.Entry;
import java.util.List;

public class QuadTreeDemo {
    public static void main(String[] args) {
        // Create QuadTree
        Rectangle bounds = new Rectangle(0, 0, 1000, 1000);
        QuadTree<String> tree = new SimpleQuadTree<>(4, bounds);
        
        // Insert points
        tree.insert(new Point(100, 200), "Point A");
        tree.insert(new Point(300, 400), "Point B");
        tree.insert(new Point(500, 600), "Point C");
        tree.insert(new Point(700, 800), "Point D");
        
        // Query by rectangle
        System.out.println("Points in region (0,0) to (500,500):");
        List<Entry<String>> rectResults = tree.query(new Point(0, 0), 500, 500);
        rectResults.forEach(e -> System.out.println("  " + e.data()));
        
        // Query by circle
        System.out.println("\nPoints within 300 units of (400, 500):");
        List<Entry<String>> circleResults = tree.query(new Point(400, 500), 300.0);
        circleResults.forEach(e -> System.out.println("  " + e.data()));
        
        // Remove and verify
        tree.remove(new Point(100, 200));
        System.out.println("\nPoint A exists: " + tree.exists(new Point(100, 200)));
    }
}
```

Output:
```
Points in region (0,0) to (500,500):
  Point A
  Point B

Points within 300 units of (400, 500):
  Point B
  Point C

Point A exists: false
```

## API Reference

### QuadTree Interface
- `Entry<T> insert(Point point, T data)` - Insert or update point
- `boolean exists(Point point)` - Check if point exists
- `boolean remove(Point point)` - Remove point
- `List<Entry<T>> query(Point point, double width, double height)` - Rectangle query
- `List<Entry<T>> query(Point point, double radius)` - Circle query

### Classes
- `SimpleQuadTree<T>` - Standard QuadTree implementation
- `GlobalQuadTree<T>` - Pre-configured for world coordinates
- `Point(double x, double y)` - 2D point record
- `Rectangle(double x, double y, double width, double height)` - Rectangle
- `Circle(Point center, double radius)` - Circle
- `Entry<T>(Point point, T data)` - Point-data pair record

### Exceptions
- `PointOutOfBoundsException` - Point outside QuadTree boundaries
- `QuadTreeException` - General QuadTree error
- `IllegalArgumentException` - Invalid parameters

---

**Need Help?** See IMPROVEMENTS.md for detailed implementation notes and ANALYSIS.md for architectural details.

