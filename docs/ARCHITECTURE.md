> **Purpose:** Explains *why* architectural choices were made.

```markdown
# System Architecture

## Design Pattern: Hybrid JSONB Model
We evaluated multiple database patterns for user management (Single Table Inheritance, Concrete Table Inheritance, and Class Table Inheritance) and selected the **Hybrid JSONB Pattern**.

### Why Hybrid JSONB?
1. **Performance & Simplicity:** Authentication queries against core columns (`username`, `password`, `role`) execute without expensive `JOIN` operations.
2. **No Schema Bloat:** Avoids sparse columns and massive blocks of `NULL` values that would plague a flat table as new user types or attributes are introduced.
3. **Extensibility:** Role-specific properties (like student majors or faculty extensions) can evolve independently inside the `metadata` JSON container without altering core table definitions or breaking JDBC mappings.