pipeline {
    agent any

    stages {
        stage('Clone Code') {
            steps {
                git branch: 'main', url: 'https://github.com/krsna-kant/K8attempt2.git'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    // Replace the following commands with your actual Docker build and push steps
                    sh 'docker build -t mark-ecommerce:latest .'
                     sh 'docker tag mark-ecommerce:latest  krsna3629/mark-ecommerce:latest'
                     sh 'docker push krsna3629/mark-ecommerce:latest'

                }
            }
        }
    }
}




