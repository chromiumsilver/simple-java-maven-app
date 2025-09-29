pipeline {
    agent any

    tools{
        maven 'maven-3.9'
    }

    environment {
        APP_NAME = 'my-app'
        NEXUS_DOMAIN = '170.64.161.251:8081'
        NEXUS_REGISTRY = '170.64.161.251:8081/docker-hosted'
    }

    stages{
        // stage('test') {
        //     steps{
        //         echo "Testing the application..."
        //         sh 'mvn test'
        //     }
        // }
        stage('increment version') {
            steps{
                echo 'Incrementing app version...'
                sh 'mvn build-helper:parse-version versions:set \
                -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                versions:commit'
            }
        }
        stage('build app') {
            steps{
                echo 'Building the application...'
                sh 'mvn clean package'
            }
        }
        stage('build image') {
            steps{
                echo 'Building docker image....'
                script {
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_TAG = "${version}-${BUILD_NUMBER}"
                }

                sh "docker build -t ${NEXUS_REGISTRY}/${APP_NAME}:${IMAGE_TAG} ."
            }
        }
        stage('push image to registry') {
            steps{
                echo 'Pushing docker image...'
                withCredentials([usernamePassword(credentialsId: 'NEXUS_DOCKER_CRED', passwordVariable: 'PASSWORD', usernameVariable: 'USER')]){
                        sh '''
                            echo ${PASSWORD} | docker login -u ${USER} --password-stdin ${NEXUS_DOMAIN}
                            docker push ${NEXUS_REGISTRY}/${APP_NAME}:${IMAGE_TAG}
                        '''
                    }
            }
        }
        stage('deploy') {
            when{
                branch 'main'
            }
            steps{
                echo 'Deploying the application...'
            }
        }
    }
}