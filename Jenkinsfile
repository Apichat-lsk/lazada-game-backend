pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Apichat-lsk/lazada-game-backend.git'
            }
        }

        stage('Build JAR') {
            steps {
                // รัน Maven ผ่าน Docker container แทน
                sh 'docker run --rm -v $PWD:/app -w /app maven:3.9.3-eclipse-temurin-17 mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t lazada-game-backend:latest .'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
        }
    }
}
