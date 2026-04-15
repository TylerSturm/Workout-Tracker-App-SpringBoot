# 💪 Workout Tracker App

A full-stack workout tracking web application built with **Java Spring Boot** and a **PostgreSQL** (Neon) database. Log your workouts, track exercises over time, and monitor your fitness progress — all in one place.

---

## 🚀 Features

- **Workout Logging** — Create, view, update, and delete workout sessions
- **Exercise Tracking** — Record exercises, sets, reps, and weights
- **Persistent Storage** — Backed by a cloud-hosted PostgreSQL database (Neon)
- **RESTful API** — Clean, well-structured backend API
- **Web UI** — HTML frontend served directly by the Spring Boot application
- **Dockerized** — Easily deployable via Docker

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java, Spring Boot |
| Database | PostgreSQL (Neon) |
| Frontend | HTML, CSS |
| Build Tool | Maven |
| Containerization | Docker |

---

## 📁 Project Structure

```
Workout-Tracker-App-SpringBoot/
├── src/
│   ├── main/
│   │   ├── java/         # Spring Boot application, controllers, services, repositories
│   │   └── resources/    # application.properties, static assets, templates
│   └── test/             # Unit and integration tests
├── Dockerfile
├── pom.xml
└── mvnw / mvnw.cmd
```

---

## ⚙️ Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+ (or use the included `mvnw` wrapper)
- A [Neon](https://neon.tech) PostgreSQL database (or any PostgreSQL instance)
- Docker (optional, for containerized deployment)

### 1. Clone the Repository

```bash
git clone https://github.com/TylerSturm/Workout-Tracker-App-SpringBoot.git
cd Workout-Tracker-App-SpringBoot
```

### 2. Configure the Database

Update `src/main/resources/application.properties` with your PostgreSQL connection details:

```properties
spring.datasource.url=jdbc:postgresql://<your-neon-host>/<your-database>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
```

### 3. Run the Application

**Using Maven wrapper:**
```bash
./mvnw spring-boot:run
```

**Or build and run the JAR:**
```bash
./mvnw clean package
java -jar target/*.jar
```

The application will be available at `http://localhost:8080`.

---

## 🐳 Docker Deployment

### Build the Image

```bash
docker build -t workout-tracker .
```

### Run the Container

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<host>/<db> \
  -e SPRING_DATASOURCE_USERNAME=<username> \
  -e SPRING_DATASOURCE_PASSWORD=<password> \
  workout-tracker
```

---

## 🔌 API Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/workouts` | Get all workouts |
| `GET` | `/api/workouts/{id}` | Get a workout by ID |
| `POST` | `/api/workouts` | Create a new workout |
| `PUT` | `/api/workouts/{id}` | Update a workout |
| `DELETE` | `/api/workouts/{id}` | Delete a workout |

---

## 🤝 Contributing

Contributions are welcome! To get started:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📄 License

This project is open source. See [LICENSE](LICENSE) for details.

---

## 👤 Author

**Tyler Sturm** — [@TylerSturm](https://github.com/TylerSturm)
