# Changelog

## [1.1.0] - 2026-03-31
### Changed
- Migrated user schema from a flat table structure to a **Hybrid JSONB Schema** using a native `metadata` column.
- Updated `UserRepository.java` query logic to extract `year_of_study` dynamically using PostgreSQL JSON operators (`metadata->>'year_of_study'`).
- Expanded database seeder script to include an `ADMIN` (`sys_admin`), `HOD` (`cs_hod`), and `STUDENT` (`st_alex`).

### Added
- Added GIN index on the `metadata` column for optimized JSON lookups.