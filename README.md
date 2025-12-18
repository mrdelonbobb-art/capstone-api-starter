# EasyShop API CapStone Project –Notes & Fixes

This repository contains backend updates and bug fixes made during development of the EasyShop application.
---
✅ Changes Made

Bug 1 Products Controller
- Replaced **create** logic with **update** logic where appropriate.
- Ensures existing products are modified instead of unintentionally creating duplicates.
---

Bug 2 🗄️ MySQLProductDao
Fixed SQL issues in product filtering logic:
- Removed **duplicate `minPrice` condition**
- Corrected SQL comparison:
  - Removed double `>=` operator in the price filter
- Ensures product search queries return accurate results

--- Front-end fixes
- Fixed pricing filter logic: in Templates Folder --> `home.html`(frontend code of application can be found in easyshopFrontend repository)
  - `minPrice` → corrected
  - Added `maxPrice` support
- Added inline comments to explain price filtering behavior

---
# 🛠️ Summary
These changes improve:
- Product update behavior
- SQL query accuracy
- Frontend price filtering clarity

All fixes were made to align backend logic, database queries, and frontend templates.

# Tech Stack
- Java 17
- Spring Boot
- Spring Security (JWT)
- MySQL

## 📌 Notes
If additional changes are made later, this README should be updated to reflect new fixes or enhancements.
