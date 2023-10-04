pipeline {
    agent {
        docker {
            image 'openjdk:17-jdk'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
         }
    }
    stages {
        stage("Clone") {
            steps {
                git branch: 'main', credentialsId: 'GIT_CREDENTIALS', url: 'https://github.com/PhumlaniDev/library-backend.git'
            }
        }

        stage('Use Google Secrets') {
            steps {
                withCredentials([file(credentialsId: 'GOOGLE_SECRETS', variable: 'GOOGLE_SECRETS')]) {
                sh "cp $GOOGLE_SECRETS src/main/resources/"
                }
            }
        }

        stage("Build") {
            steps {
                script {
                    def mavenHome = tool name: "Maven-3.9.4", type: "maven"
                    def mavenCMD = "${mavenHome}/bin/mvn"
                    sh "${mavenCMD} clean install"
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
                        def mavenHome = tool name: "Maven-3.9.4", type: "maven"
                        def mavenCMD = "${mavenHome}/bin/mvn"
                        sh "${mavenCMD} sonar:sonar"
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