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
                    sh 'docker build -t mark_to_do:latest .'
                    sh 'docker build -t mark_to_do:v2.$BUILD_ID .'  
                    sh 'docker tag mark_to_do:v2.$BUILD_ID krsna3629/mark_to_do:v2.$BUILD_ID'
                    sh 'docker tag mark_to_do:latest krsna3629/mark_to_do:latest'
                }
            }
        }

        stage('Pushing Image to DockerHub') {
            steps {
                withCredentials([string(credentialsId: 'DHPass', variable: 'DHPass')]) {
                    sh 'docker login -u krsna3629 -p ${DHPass}'
                    sh 'docker image push krsna3629/mark_to_do:v2.$BUILD_ID'
                    sh 'docker image push krsna3629/mark_to_do:latest'
                }
            }
        }

        stage('Send Files to Ansible & K8 Servers') {
            steps {
                sh 'scp -o StrictHostKeyChecking=no /var/lib/jenkins/workspace/k8deployment/ansible-playbook.yml ubuntu@3.110.224.192:/home/ubuntu/'
                sh 'scp -o StrictHostKeyChecking=no /var/lib/jenkins/workspace/k8deployment/service.yml ubuntu@3.111.245.94:/home/ubuntu/'
                sh 'scp -o StrictHostKeyChecking=no /var/lib/jenkins/workspace/k8deployment/deployment.yml ubuntu@3.111.245.94:/home/ubuntu/'
            }
        }

        stage('Run Ansible Playbook') {
            steps {
                sshagent(['Ansible']) {
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.110.224.192 "ansible-playbook /home/ubuntu/ansible-playbook.yml"'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sshagent(['K8']) {
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.111.245.94 "kubectl apply -f /home/ubuntu/service.yml && kubectl apply -f /home/ubuntu/deployment.yml"'
                }
            }
        }

        stage('Run Local Ansible Playbook') {  // Newly added stage
            steps {
                script {
                    def inventoryFile = ' my_inv'
                    def playbookFile = 'ansible-playbook.yml'

                    // Run the ansible-playbook locally
                    sh """
                    ansible-playbook -i ${inventoryFile} ${playbookFile}
                    """
                }
            }
        }
    }
}
