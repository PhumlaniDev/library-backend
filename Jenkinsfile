pipeline {
    agent {
        docker { image 'node:16-alpine' }
    }
    stages {
        stage('Clone') {
            steps {
                git credentialsIs: 'GIT_CREDENTIALS' 'https://github.com/PhumlaniDev/library-backend.git'
            }
        }

        stage('Use Google Secrets') {
                    steps {
                        withCredentials([file(credentialsId: 'your-credential-id', variable: 'SECRET_FILE')]) {
                            sh 'cp $SECRET_FILE src/main/resources/'
                        }
                    }
                }

        stage('Build') {
            steps {
                def mavenHome tool name: "Maven-3.9.4" type: "maven"
                def mavenCMD = "${mavenHome}/bin/mvn"
                sh '${mavenCMD} clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Code Coverage') {
            steps {
                withSonarQubeEnv('SonarQube 9.9') {
                    def mavenHome tool name: "Maven-3.9.4" type: "maven"
                    def mavenCMD = "${mavenHome}/bin/mvn"
                    sh '${mavenCMD} sonar:sonar'
            }
        }

        stage('Dockerize') {
            steps {
                // Build a Docker image for the Spring Boot application
                sh 'docker build -t your-docker-image-name .'
            }
        }

        stage('Push to DockerHub') {
            steps {
                // Push the Docker image to DockerHub (make sure you are logged in)
                sh 'docker push yourusername/your-docker-image-name'
            }
        }
    }
}