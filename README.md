# 📚 My Study Planner — Backend

> A full-stack study management application that helps students organize tasks, track subjects, manage weekly schedules, and receive automated deadline reminders.

🔗 **Live Demo:** https://hannahstudyplanner.vercel.app  
🖥️ **Frontend Repo:** https://github.com/hannahrajapaga-web/study-planner-client  

---

## ✨ Features

- 🔐 **JWT Authentication** — Secure register and login with token-based auth
- 📚 **Subject Management** — Create and manage subjects with custom color themes
- ✅ **Task Management** — Add, edit, complete, and delete tasks with deadlines
- 📅 **Weekly Schedule Planner** — Plan study blocks for each day of the week
- 🔔 **In-App Notifications** — Real-time deadline alerts in the navbar bell icon
- 📧 **Email Notifications** — Automated deadline reminder emails via Gmail SMTP
- 🍅 **Pomodoro Timer** — Built-in 25/5 minute focus timer
- 📊 **Dashboard Analytics** — Task completion rate, subject count, scheduled classes
- ⚠️ **Overdue Detection** — Highlights overdue tasks in red on the dashboard

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React 18, Vite, React Router, Axios |
| Backend | Spring Boot 3, Spring Security, Spring Mail |
| Database | MySQL 8 |
| ORM | Hibernate / JPA |
| Authentication | JWT (JSON Web Tokens) |
| Containerization | Docker |
| Frontend Hosting | Vercel |
| Backend Hosting | Render (Docker) |
| Database Hosting | Railway |

---

## 📁 Project Structure

```
study-planner-server/
├── src/
│   └── main/
│       └── java/
│           └── com/example/studyplanner/
│               ├── config/
│               │   ├── JwtAuthenticationFilter.java
│               │   ├── JwtTokenProvider.java
│               │   └── SecurityConfig.java
│               ├── controller/
│               │   ├── AuthController.java
│               │   ├── NotificationController.java
│               │   ├── StudyScheduleController.java
│               │   ├── SubjectController.java
│               │   ├── TaskController.java
│               │   └── UserController.java
│               ├── model/
│               │   ├── Notification.java
│               │   ├── StudySchedule.java
│               │   ├── Subject.java
│               │   ├── Task.java
│               │   └── User.java
│               ├── repository/
│               ├── service/
│               └── StudyPlannerServerApplication.java
├── Dockerfile
└── pom.xml
---

## 🔌 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Tasks
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks for user |
| POST | `/api/tasks` | Create new task |
| PUT | `/api/tasks/{id}` | Update task |
| PUT | `/api/tasks/{id}/toggle` | Toggle task completion |
| DELETE | `/api/tasks/{id}` | Delete task |

### Subjects
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/subjects` | Get all subjects |
| POST | `/api/subjects` | Create subject |
| DELETE | `/api/subjects/{id}` | Delete subject |

### Schedules
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/schedules` | Get weekly schedule |
| POST | `/api/schedules` | Add schedule block |
| DELETE | `/api/schedules/{id}` | Delete schedule block |

### Notifications
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications` | Get all notifications |
| PUT | `/api/notifications/{id}/read` | Mark as read |

---

## 🚀 Running Locally

### Prerequisites
- Java 17
- Maven
- MySQL 8

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/hannahrajapaga-web/study-planner.git
cd study-planner
```

**2. Create the database**
```sql
CREATE DATABASE study_planner_db;
```

**3. Configure `src/main/resources/application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/study_planner_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
jwt.secret=your_jwt_secret
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**4. Run the application**
```bash
./mvnw spring-boot:run
```

Backend runs on `http://localhost:8080`

---

## 🐳 Docker

```bash
docker build -t study-planner-server .
docker run -p 8080:8080 study-planner-server
```

---

## 🔒 Security

- Passwords hashed with BCrypt
- JWT tokens expire after 24 hours
- All endpoints protected except `/api/auth/**`
- CORS configured for frontend origin only
- Sensitive config via environment variables

---

## 📧 Email Notifications

The scheduler runs every hour and checks for tasks due within 24 hours. For each upcoming task:
1. Creates an in-app notification
2. Sends an email reminder to the user's registered email address
3. Prevents duplicate notifications for the same task

---

## 👩‍💻 Developer

**Hannah** — B.Tech CSE, Anurag University  
GitHub: [@hannahrajapaga-web](https://github.com/hannahrajapaga-web)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
