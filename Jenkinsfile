pipeline {
    agent {
        docker {
            image 'maven:3.9.3-eclipse-temurin-17'
            args '-v $HOME/.m2:/root/.m2'  // optional สำหรับ cache Maven dependencies
        }
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Apichat-lsk/lazada-game-backend.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
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
