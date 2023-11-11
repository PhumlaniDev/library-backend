pipeline {
    agent any
    environment {
        GOOGLE_SECRETS = credentials('GOOGLE_SECRETS')
    }

    stages {
        
        stage('Clean Workspace') {
            steps {
                script {
                    deleteDir()
                }
            }
        }
        
        stage("Clone") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'GIT_CREDENTIALS', url: 'https://github.com/PhumlaniDev/library-backend.git']])
            }
        }

        stage('Use Google Secrets') {
            steps {
                sh "cp \$GOOGLE_SECRETS ~/.jenkins/workspace/library-pipeline/src/main/resources"
            }
        }

        stage("Build") {
            steps {
                script {
                    sh "mvn clean install"
                }
            }
        }

        stage("Test") {
            steps {
                sh "mvn test"
            }
        }

        stage("Package") {
            steps {
                sh "mvn package"
            }
        }

        stage("Code Coverage") {
            steps {
                withSonarQubeEnv("SonarQube 9.9") {
                    script{
                        sh "mvn sonar:sonar"
                    }
                }
            }
        }

        stage("Dockerize") {
            steps {
                // Build a Docker image for the Spring Boot application
                sh "docker build -t library-management-backend ."
            }
        }

        stage("Push to DockerHub") {
            steps {
                withCredentials([file(credentialsId: "DOCKER_HUB_PWD", variable: "DOCKER_HUB_PWD")]) {
                    sh "docker login -u aphumlanidev -p ${DOCKER_HUB_PWD}"
                    sh "docker push aphumlanidev/library-management-backend"
                }
            }
        }
    }
}
