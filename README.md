# ⚙️ Skill Agent Backend (Spring Boot)

## 📌 Overview

This is the backend service for the AI Skill Assessment Agent.

It handles:

* Resume & Job Description processing
* Skill extraction and comparison
* Proficiency scoring
* Learning plan generation

---

## ⚙️ Tech Stack

* Java
* Spring Boot
* REST APIs

---

## 🧠 Core Logic

### 1. Skill Extraction

* Extract skills from Job Description
* Extract skills from Resume

### 2. Skill Matching & Scoring

* Match skills between Resume and JD
* Score based on:

  * Frequency
  * Context (projects, experience)

### 3. Proficiency Classification

* 0–3 → Beginner
* 4–7 → Intermediate
* 8+ → Advanced

### 4. Gap Analysis

* Identify missing or weak skills

### 5. Learning Plan Generation

* Suggest adjacent and achievable skills
* Provide:

  * Resources
  * Estimated time

---

## 🔗 Frontend

👉 https://github.com/HarshithMaddela/skill-agent-frontend

---

## ▶️ Running Locally

```bash
git clone https://github.com/HarshithMaddela/skill-agent-backend
cd skill-agent-backend
mvn spring-boot:run
```

Server runs on:
👉 http://localhost:8080

---

## 📡 API Endpoints

### POST /analyze

**Input:**

* Resume text
* Job Description text

**Output:**

* Skill scores
* Skill levels
* Gap analysis
* Learning plan

---

## 📂 Project Structure

```
src/main/java/
 ├── controller/
 ├── service/
 ├── model/
 └── repository/
```

---

## 👤 Author

Harshith Sai Krishna Maddela

---
