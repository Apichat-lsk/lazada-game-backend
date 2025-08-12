pipeline {
    agent any

    environment {
        IMAGE_NAME = "lazada-game-app"
        CONTAINER_NAME = "lazada-game-app-container"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Apichat-lsk/lazada-game-backend.git'
            }
        }

        stage('List workspace files') {
            steps {
                sh 'ls -l'
                sh 'ls -l docker-compose.yml Dockerfile || true'
                sh 'docker --version'
                sh 'docker compose version || true'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh "docker compose down || true"
                sh "docker compose up -d --build"
            }
        }
    }
}
