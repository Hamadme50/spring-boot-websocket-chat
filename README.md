# Spring Boot WebSocket Chat — Realtime, Scalable, Multi-Room Chat

A production-ready WebSocket chat built with Spring Boot that runs **fast on low-spec VPS** (low cores/RAM) and scales horizontally. Create **multiple chat rooms**, each with its **own embeddable link**, **role/rank system** (Owner, Admin, Moderator, User, Guest), **per-room customization**, and **realtime messaging** with minimal latency.

**Live Demo:** [https://chat-links.com/](https://chat-links.com/)

[![DEMO](https://i.ibb.co/cX1vYftP/Screenshot-20250922-021659-Chrome.png)](https://chat-links.com)

Contact me if needed
https://upwork.com/freelancers/~0194332d59dcc18028

## Table of Contents

* [Overview](#overview)
* [Key Features](#key-features)
* [Architecture](#architecture)
* [Tech Stack](#tech-stack)
* [Performance & Scaling](#performance--scaling)
* [Screens & UX](#screens--ux)
* [Requirements](#requirements)
* [Quick Start](#quick-start)
* [Installation — Windows](#installation--windows)
* [Installation — Linux](#installation--linux)
* [Configuration](#configuration)

  * [MongoDB credentials (`MongoConfig.java`)](#mongodb-credentials-mongoconfigjava)
  * [MySQL credentials (`application.properties`)](#mysql-credentials-applicationproperties)
  * [Redis](#redis)
  * [RabbitMQ (optional) & `WebSocketConfig.java`](#rabbitmq-optional--websocketconfigjava)
* [Build & Run](#build--run)
* [Embedding Rooms Anywhere](#embedding-rooms-anywhere)
* [Roles & Permissions](#roles--permissions)
* [Security & Moderation](#security--moderation)
* [Troubleshooting](#troubleshooting)
* [FAQ](#faq)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)

---

## Overview

This project is a **realtime WebSocket chat** that supports **huge concurrent user counts** under tight resource budgets. It’s optimized for **low-latency delivery**, **efficient message fan-out**, and **simple ops**. Every room gets a **unique link** you can **embed anywhere** (websites, blogs, landing pages, portals).

---

## Key Features

* **Realtime WebSocket chat** (SockJS & STOMP supported)
* **Multi-room**: create unlimited rooms; each room has a **shareable, embeddable URL**
* **Ranks per room**: **Owner → Admin → Moderator → User → Guest**
* **Per-room Admin Panel**: theme, banner, rules, filters, slow-mode, invites
* **Scalable storage**:

  * **MongoDB** for chat messages/history
  * **MySQL** for users, rooms, and role data
  * **Redis** for presence, rate limiting, caching, and pub/sub
* **RabbitMQ (optional)** for cross-node broadcast when you scale horizontally
* **Fast & resource-light**: tuned for low VPS cores/RAM
* **Embeddable** widget/iframe for any site
* **REST hooks** for management/integrations (room lifecycle, moderation)

---

## Architecture

* **Spring Boot WebSocket** server for connections, messaging routes, and auth guards
* **Redis**: presence, rate limits, message fan-out/caching
* **MongoDB**: append-only message storage, history pagination
* **MySQL**: relational data (users, rooms, roles, bans, configs)
* **RabbitMQ (optional)**: durable broker for multi-instance broadcasting
* **Stateless app nodes** behind a reverse proxy → easy horizontal scale

---

## Tech Stack

* **Java 17+**, **Spring Boot**
* **WebSocket** (SockJS + STOMP compatible)
* **Maven**
* **MySQL** (or MariaDB)
* **MongoDB**
* **Redis**
* **RabbitMQ** *(optional; toggled in code)*

---

## Performance & Scaling

* Use **Redis pub/sub + caching** to reduce DB hits and accelerate broadcasts
* Store chat history in **MongoDB** with indexes on room/time
* Keep relational pieces (users/rooms/roles) in **MySQL** with proper indexing
* For a **1 vCPU / 1–2 GB RAM VPS**:

  * JVM: `-Xms256m -Xmx512m` (adjust per load)
  * Enable slow-mode & anti-spam in busy public rooms
  * Paginate history fetches (limit page size)
* For **multi-node** deployments: enable **RabbitMQ** and run multiple app instances behind Nginx/HAProxy; share Redis/Mongo/MySQL

---

## Screens & UX

* **Lobby / Room list** (create/join rooms)
* **Room view** (live messages, presence, pinned notices)
* **Admin Panel** (per-room configuration, theme, filters, roles)
* **Moderation tools** (mute/kick/ban, slow-mode, word filters)

*(Screens will appear here if you add images to the repo’s `/assets` folder.)*

---

## Requirements

* **Java 17+**
* **Maven 3.9+** (or the included Maven wrapper)
* **MySQL 8+** (or MariaDB 10.6+)
* **MongoDB 6+**
* **Redis 6+**
* **RabbitMQ 3.12+** *(optional for horizontal scaling)*

---

## Quick Start

```bash
# clone
git clone https://github.com/Hamadme50/spring-boot-websocket-chat.git
cd spring-boot-websocket-chat

# configure credentials (see "Configuration" below)

# run in dev
./mvnw spring-boot:run     # Linux/Mac
# or
mvnw.cmd spring-boot:run   # Windows
```

Open `http://localhost:8080` and create your first room.

---

## Installation — Windows

1. **Install Java & Maven**
   Verify with:

```powershell
java -version
mvn -version
```

2. **Import database.sql MySQL**

```sql
mysql -u root -p mydb < /path/to/database.sql
```

3. **Install MongoDB**

```javascript
use chatmongo
db.createUser({
  user: "chatmongoUser",
  pwd: "AnotherStrongPassword!",
  roles: [ { role: "readWrite", db: "chatmongo" } ]
})
```

4. **Install Redis** (default port **6379**)

5. **(Optional) Install RabbitMQ** (enable management plugin if desired)

6. **Configure the app** (see [Configuration](#configuration)) and run:

```powershell
mvnw.cmd spring-boot:run
```

---

## Installation — Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk maven mysql-server redis-server
sudo systemctl enable --now mysql redis-server

# MySQL
**Import database.sql MySQL**

```sql
mysql -u root -p mydb < /path/to/database.sql
```

# MongoDB (install from official repo or distro)
sudo systemctl start mongod
mongosh <<'MJS'
use chatmongo
db.createUser({
  user: "chatmongoUser",
  pwd: "AnotherStrongPassword!",
  roles: [ { role: "readWrite", db: "chatmongo" } ]
})
MJS
```

---

## Configuration

> Paths below refer to files inside this repository.

### MongoDB credentials (`MongoConfig.java`)

Set your Mongo URI and DB name in:

```
src/main/java/.../config/MongoConfig.java
```

Example:

```java
private static final String MONGO_URI = "mongodb://chatmongoUser:AnotherStrongPassword!@localhost:27017/chatmongo";
private static final String DB_NAME   = "chatmongo";
```

*(If your code reads env vars, set `MONGO_URI` and `MONGO_DB` accordingly.)*

### MySQL credentials (`application.properties`)

Edit:

```
src/main/resources/application.properties
```

Add/update:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/chatdb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC
spring.datasource.username=chatuser
spring.datasource.password=StrongPassword!
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server
server.port=8080

# Mongo (if configured via Spring props)
spring.data.mongodb.uri=mongodb://chatmongoUser:AnotherStrongPassword!@localhost:27017/chatmongo

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
# spring.data.redis.password=...
```

> Use **utf8mb4** for full emoji support.

### Redis

Minimum required:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

Used for presence, pub/sub fan-out, caching, and rate limiting.

### RabbitMQ (optional) & `WebSocketConfig.java`

To broadcast messages across **multiple app instances**, enable RabbitMQ.

* Open:

```
src/main/java/.../config/WebSocketConfig.java
```

* **Uncomment** the RabbitMQ integration lines and configure host/credentials.

Example properties:

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

---

## Build & Run

**Development**

```bash
mvn spring-boot:run
```

**Production (publish)**

```bash
sudo apt-get install screen
screen -r chatlinks
cd /path to your project
mvn spring-boot:run
```

---

## Roles & Permissions

| Rank      | Manage Room | Manage Users | Moderate (mute/ban) | Post | View |
| --------- | ----------- | ------------ | ------------------- | ---- | ---- |
| Owner     | ✅           | ✅            | ✅                   | ✅    | ✅    |
| Admin     | ✅           | ✅            | ✅                   | ✅    | ✅    |
| Moderator | ❌ (limited) | ✅ (limited)  | ✅                   | ✅    | ✅    |
| User      | ❌           | ❌            | ❌                   | ✅    | ✅    |
| Guest     | ❌           | ❌            | ❌                   | ✅\*  | ✅    |

\* Guests may have posting limits or require captcha/slow-mode depending on room settings.

---

## Security & Moderation

* **Role-based access control** (per room)
* **Slow-mode & rate limiting** (backed by Redis)
* **Filtered words & anti-spam**
* **Kick/Ban** (temporary or permanent)
* **Private / invite-only / password-protected** rooms
* **Audit trail** (if enabled) for moderator actions

---

## Troubleshooting

**WebSocket handshake fails behind proxy**

* Ensure upgrade headers are forwarded:
  `Upgrade`, `Connection`, `Sec-WebSocket-Key`, `Sec-WebSocket-Version`
* Nginx example:

  ```nginx
  proxy_set_header Upgrade $http_upgrade;
  proxy_set_header Connection "upgrade";
  ```

**Messages don’t cross between app instances**

* Verify Redis connectivity and/or enable RabbitMQ in `WebSocketConfig.java`
* Confirm matching broker credentials and reachable host

**MongoDB auth errors**

* Double-check `MongoConfig.java` or `spring.data.mongodb.uri`
* Test with `mongosh` connection string

**MySQL connection refused**

* Validate `spring.datasource.url`, user, and privileges
* Ensure MySQL binds on `0.0.0.0` if remote access is needed

**High memory on tiny VPS**

* Lower JVM heap (`-Xmx384m`)
* Reduce history page size and enable slow-mode in busy rooms

---

## FAQ

**Can this run on 1 vCPU / 1 GB RAM?**
Yes. Start all services on the same host, keep history pagination small, enable slow-mode. For growth, move Redis/Mongo/MySQL to managed services and add RabbitMQ.

**Do I need RabbitMQ?**
Not for a single instance. It’s recommended when running **multiple** app nodes so broadcasts reach users connected to other nodes.

**Where do I set MongoDB credentials?**
In `MongoConfig.java` (or via `spring.data.mongodb.uri`), see [Configuration](#configuration).

**Where do I set MySQL credentials?**
In `src/main/resources/application.properties`, see [Configuration](#configuration).

**How do I customize a room?**
Open the **Admin Panel** for that room to edit theme, banners, filters, slow-mode, and roles.

---

## Roadmap

* OAuth/Social login (Google/GitHub)
* File/image sharing with virus scanning
* Push notifications & offline mentions
* Webhooks for external moderation/analytics
* Docker Compose examples (app + Redis + MongoDB + MySQL + RabbitMQ)

---

## Contributing

1. Fork the repo
2. Create your feature branch: `git checkout -b feature/awesome`
3. Commit changes: `git commit -m "feat: add awesome"`
4. Push branch: `git push origin feature/awesome`
5. Open a Pull Request

---

## License

**MIT** — use it in commercial and open-source projects.

---

### Author Notes

contact me 
https://upwork.com/freelancers/~0194332d59dcc18028

* If you rename databases, update both **`MongoConfig.java`** and **`application.properties`**.
* For RabbitMQ support, **uncomment** the code in **`WebSocketConfig.java`** and add the `spring.rabbitmq.*` properties shown above.
* Ensure **Nginx/Cloudflare** pass WebSocket upgrades when deploying behind a proxy or CDN.

— end of README —

