# SkillBridge AI - GenAI Resume Analyzer and Mock Interview Coach

SkillBridge AI is a full-stack GenAI web application that helps users analyze resumes, compare resumes with job descriptions, generate ATS scores, identify missing skills, and prepare for interviews using AI.

## Features

- User registration and login
- Resume PDF upload
- Resume text extraction using Apache PDFBox
- Basic ATS resume analysis
- GenAI resume analysis using Gemini API
- Resume-based AI question answering
- Job description matching
- AI job description matching
- Mock interview question generation
- AI interview answer feedback
- Activity history for resumes, reports, job matches, and interview feedback

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Data JPA
- REST APIs
- MySQL
- Apache PDFBox
- Gemini API

### Frontend
- HTML
- CSS
- JavaScript

### Database
- MySQL

## Project Structure

```text
SkillBridge-AI
│
├── skillbridge-backend
│   ├── src/main/java/com/skillbridge
│   │   ├── controller
│   │   ├── dto
│   │   ├── model
│   │   ├── repository
│   │   ├── service
│   │   └── SkillbridgeBackendApplication.java
│   │
│   ├── src/main/resources
│   │   ├── application.properties
│   │   └── application-example.properties
│   │
│   ├── pom.xml
│   └── README.md
│
└── skillbridge-frontend
    ├── index.html
    ├── register.html
    ├── login.html
    ├── dashboard.html
    ├── css
    │   └── style.css
    └── js
        ├── auth.js
        └── dashboard.js