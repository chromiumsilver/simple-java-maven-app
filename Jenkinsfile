pipeline {
    agent any

    // tools{
    //     maven 'maven-3.9'
    // }

    stages{
        stage('test') {
            steps{
                echo "Testing the application..."
                // sh 'mvn test'
            }
        }
        stage('build') {
            when{
                branch 'main'
            }
            steps{
                echo 'Building the application...'
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