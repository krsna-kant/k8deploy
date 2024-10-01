pipeline {
    agent any

    stages {
        stage('Clone Code') {
            steps {
                // Clone code from the GitHub repository
                git branch: 'main', url: 'https://github.com/krsna-kant/K8attempt2.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    // Build Docker image with the tag mark_to_do:latest
                    sh 'docker build -t krsna3629/mark_to_do:latest .'
                }
            }
        }

        stage('Pushing Image to DockerHub') {
            steps {
                // Use Jenkins credentials for Docker Hub authentication
                withCredentials([string(credentialsId: 'DHPass', variable: 'DHPass')]) {
                    sh '''
                        echo $DHPass | docker login -u krsna3629 --password-stdin
                        docker image tag mark_to_do:latest krsna3629/mark_to_do:v1.$BUILD_ID
                        docker image push krsna3629/mark_to_do:v1.$BUILD_ID
                        docker image push krsna3629/mark_to_do:latest
                    '''
                }
            }
        }
    }
}
