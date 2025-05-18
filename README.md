# 🗓️ PlanMate

**PlanMate** Plan-Mate is a **Kotlin-based command-line interface (CLI)** task management application designed to streamline project and task tracking. Built with **Clean Architecture** and **SOLID principles**, it supports role-based access control, audit logging, and a flexible data storage system, currently transitioning from **CSV** to **MongoDB**. **Plan-Mate** is ideal for teams needing a lightweight, extensible tool to manage tasks

---

## 📦 Features
- ✅ Task Management: Create, update, and track tasks with states (e.g., `TODO`, `In Progress`, `Done`).
- 📁 Project Management: Organize tasks under projects.
- 📋 Role-Based Access Control: Admin and User roles, with Admins able to manage users and projects.
- 🔁 Audit Logging: Track changes to tasks and projects (e.g., state transitions), with details on who made the change, when, and what changed.
- 📊 Swimlane View: Visualize tasks by state in a CLI-based swimlane board.
- 💾 Flexible Storage: Supports MongoDB (current) and CSV (legacy), with Dependency Inversion for easy data source swapping.
- 💻 CLI Interface: Intuitive command-line interface for all operations, including viewing audit logs and managing tasks.
- 🧪 TDD-driven development using `JUnit 5`, `MockK`, and `Truth`

---

## 🧱 Architecture

### Plan-Mate follows Clean Architecture to ensure maintainability, testability, and extensibility. The project is organized into three layers: - ###

**- Domain Layer (logic)**
- Contains business logic, entities (e.g., Audit, Task, Project), use cases (e.g., CreateAuditUseCase, GetTasksByProjectIdUseCase), and repository interfaces (e.g., AuditRepository).
- Framework-agnostic, ensuring independence from storage or UI.



**- Data Layer (data)**
- Implements data access with repositories (e.g., AuditRepositoryImpl) and data sources (e.g., MongoAuditDataSource, planned CsvAuditDataSource).
- Uses mappers (e.g., AuditMapper) to convert Domain entities to storage models (e.g., AuditDto).
- Supports the CSV-to-MongoDB migration via Dependency Inversion.



**- Presentation Layer (presentation)**
- Manages CLI interaction using components like GetAuditForProjectUI and GetAuditForTaskUI.
- Depends only on Domain use cases, adhering to Clean Architecture’s dependency rule.


**- DI**
 - Koin-based dependency injection




### Design Principles ###


**- Clean Architecture: -** 
- Uni-directional dependencies (Presentation → Domain ← Data), ensuring the Domain layer remains pure.

**- SOLID Principles: -**
- Single Responsibility: Each class has one responsibility (e.g., AuditMapper for mapping, MongoAuditDataSource for storage).
- Open/Closed: Extensible data sources (e.g., adding CSV without changing repositories).
- Liskov Substitution: Data sources (e.g., MongoAuditDataSource) are substitutable for their interface.
- Interface Segregation: Focused interfaces (e.g., AuditRepository).
- Dependency Inversion: Use cases and repositories depend on abstractions, not concrete implementations.
- Testability: 100% test coverage with unit and integration tests, using Koin for dependency injection.



---

## ⚙️ Technologies Used

| Layer         | Tech Stack                                     |
|---------------|------------------------------------------------|
| Language      | Kotlin                                          |
| Build Tool    | Gradle                                          |
| Architecture  | Clean Architecture + SOLID principles          |
| Dependency Injection | [Koin](https://insert-koin.io/)         |
| Database      | CSV (initial), MongoDB (current)               |
| Testing       | JUnit 5, MockK, Truth                          |
| Date/Time     | `kotlinx.datetime`, `kotlinx.uuid`             |

---

## 📚 Learning Objectives
This project was developed as a learning exercise in:

- Applying Clean Architecture in Kotlin

- Practicing SOLID design

- Building a layered CLI system

- Integrating with multiple persistence mechanisms

- Writing maintainable and testable code

---

## 🚀 Getting Started

### Prerequisites

- [JDK 17+](https://www.java.com/en/)
- [MongoDB](https://www.mongodb.com/) (optional, if using MongoDB data source)
- Gradle: 8.0 or higher (build tool)


## Setup Instructions ##
1. Clone the Repository:
```bash
git clone https://github.com/abu-dhabi-squad/Plan-Mate.git
cd Plan-Mate
```
2. Set Up MongoDB:
- Install MongoDB locally or use a cloud instance (e.g., MongoDB Atlas).
- Update the MongoDB connection string in src/main/kotlin/di/RepositoryModule.kt:
```properties
mongodb.uri=mongodb://localhost:27017/planmate
```
3. Build the Project:
```bash
./gradlew build
```
4. Run the Application:
```bash
./gradlew run
```
5. Run Tests:
```bash
./gradlew test
```


---

## 🧪 Testing
PlanMate includes comprehensive unit tests across all layers. Highlights:

- Given/When/Then style tests

- UI interaction tests with MockK

- Domain use case tests

- CSV & MongoDB data source mocking


---


## 🧑‍💻 Authors
- Abdallah Mohamed **(Mentor)**
- Alaa khaled
- Amr Ashraf
- Eslam Mohamed
- Elsayed Magdy
- Mohamed Emad
- Nour Elhoda Ahmed
- Nour Serry
- Nour Mohamed
- Shahd Hatem

---

## Contributing ##

**Contributions are welcome! To contribute:**
1. Fork the repository.
2. Create a feature branch (git checkout -b feature/your-feature).
3. Commit changes (git commit -m "Add your feature").
4. Push to the branch (git push origin feature/your-feature).
5. Open a pull request.

**Please ensure:**
1. Code follows Kotlin conventions and Clean Architecture principles.
2. Tests are added/updated to maintain 80% or more coverage.
3. Changes are documented in the PR description.



## 📝 License
This project is licensed under **the-chance** License.

---

## ❤️ Acknowledgements
- The Chance

- Clean Architecture by Uncle Bob




Built with ❤️ by the Abu Dhabi Squad