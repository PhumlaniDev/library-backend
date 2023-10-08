pipeline {
    agent {
        label 'docker-slave'
    }

    stages {
        stage("Clone") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'GIT_CREDENTIALS', url: 'https://github.com/PhumlaniDev/library-backend.git']])
            }
        }

        stage('Use Google Secrets') {
            steps {
                script {
                    // Store the credentials in a variable
                    def secretsFile = credentials('GOOGLE_SECRETS')

                    // Check if the credentials were successfully retrieved
                    if (secretsFile != null) {
                        def sourcePath = secretsFile.toString()
                        def destinationPath = "${WORKSPACE}/src/main/resources/"

                        // Create the destination directory
                        sh "mkdir -p ${destinationPath}"

                        // Copy the secrets file to the destination directory
                        sh "cp ${sourcePath} ${destinationPath}"

                        // List the contents of the destination directory
                        sh "ls -l ${destinationPath}"
                    } else {
                        error "Failed to retrieve GOOGLE_SECRETS"
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
                withCredentials([file(credentialsId: "DOCKER_HUB_PWD", variable: "DOCKER_HUB_PWD")]) {
                    sh "docker login -u aphumlanidev -p ${DOCKER_HUB_PWD}"
                    sh "docker push aphumlanidev/library-management-backend"
                }
            }
        }
    }
}