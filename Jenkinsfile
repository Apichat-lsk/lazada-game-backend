pipeline {
    agent any

    environment {
        IMAGE_NAME = "springboot-app"
        CONTAINER_NAME = "springboot-container"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/Apichat-lsk/lazada-game-backend.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh "docker compose down"
                sh "docker compose up -d"
            }
        }
    }
}
