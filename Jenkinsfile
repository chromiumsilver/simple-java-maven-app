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
        stage('commit version change') {
            steps{
                withCredentials([usernamePassword(credentialsId: 'GITHUB_TOKEN', passwordVariable: 'TOKEN', usernameVariable: 'USERNAME')]) {
                    sh '''
                        # only need to be configured once, can also be configured inside Jenkins Server
                        git config --global user.email "jenkins@mycompany.com"
                        git config --global user.name "jenkins ci"

                        # set https remote with token auth
                        git remote set-url origin https://${USERNAME}:${TOKEN}@github.com/chromiumsilver/simple-java-maven-app.git
                        
                        git add pom.xml
                        git commit -m "version bump [ci skip]"
                        git push origin HEAD:${BRANCH_NAME}
                    ''' 
                }
            }
        }
    }
}