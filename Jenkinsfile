pipeline {
    agent {
        docker {
            label 'docker-agent'
         }
    }

    tools {
        maven "Maven-3.9.4"
    }

    stages {
        stage("Clone") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'GIT_CREDENTIALS', url: 'https://github.com/PhumlaniDev/library-backend.git']])
            }
        }

        stage('Use Google Secrets') {
            steps {
                withCredentials([file(credentialsId: 'GOOGLE_SECRETS', variable: 'SECRET_FILE')]) {
                    script {
                        def sourcePath = sh(script: "echo \$SECRET_FILE", returnStdout: true).trim()
                        def destinationPath = "${WORKSPACE}/src/main/resources/"
                        sh "rm -rf ${destinationPath}"
                        sh "mkdir -p ${destinationPath}"
                        sh "cp ${sourcePath} ${destinationPath}"
                        sh "ls -l ${destinationPath}"
                    }
                }
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
                withCredentials([file(credentialsId: "your-credential-id", variable: "DOCKER_HUB")]) {
                    sh "docker login -u aphumlanidev -p ${DOCKER_HUB}"
                    sh "docker push aphumlanidev/library-management-backend"
                }
            }
        }
    }
}