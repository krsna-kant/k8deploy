pipeline {
    agent any

    stages {
        stage('Clone Code') {
            steps {
                git branch: 'main', url: https://github.com/Ab-D-ev/kubernetes-devops-project.git
            }
        }
    }
}

