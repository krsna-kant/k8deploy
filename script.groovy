pipeline {
    agent any

    stages {
        stage('Clone Code') {
            steps {
                git branch: 'main', url: 'https://github.com/Ab-D-ev/kubernetes-devops-project.git'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    // Replace the following commands with your actual Docker build and push steps
                    sh 'docker build -t mark-ecommerce:latest .'
                }
            }
        }
    }
}




