pipeline {
    agent any

    stages {
        stage('Clone Code') {
            steps {
                git branch: 'main', url: 'https://github.com/krsna-kant/K8attempt2.git'
            }
        }
        // stage('Build Docker Image') {
        //     steps {
        //         script {
        //             echo 'Building Docker image...'
        //             // Replace the following commands with your actual Docker build and push steps
        //             sh 'docker build -t mark_to_do:latest .'  
        //         }
        //     }
        // }
        // stage('Pushing Image to DockerHub'){
        //         steps{
        //         withCredentials([string(credentialsId: 'DHPass', variable: 'DHPass')]) {
        //         sh 'docker login -u krsna3629 -p ${DHPass}'
        //         sh 'docker image push krsna3629/$JOB_NAME:v1.$BUILD_ID'
        //         sh 'docker image push krsna3629/$JOB_NAME:latest'
        //      }
        //    }
        // }
        // stage('sending File to Ansible & K8 Servers'){
        //     steps{
        //          sh 'scp -o StrictHostKeyChecking=no  /var/lib/jenkins/workspace/k8deployment/ ubuntu@35.154.235.112:/home/ubuntu/'
        //     }
        // }
        stage('Sending file to Ansible'){
            steps{
                sshagent(['Ansible']) {
                     sh 'ssh -o StrictHostKeyChecking=no ubuntu@35.154.235.112 "ls" '
                }

            }
        }
        stage('Deploy to Kubernetes'){
            steps{
                sshagent(['K8']){
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@43.205.229.90 "ls" '
                }
            }
        }
    }
}




