node {
    checkout scm
    sh 'git submodule update --init' 
    stage ('build') {
        sh 'make clean'
        sh './build.sh'
    }
    stage ('docker image') {
        sh 'make build'
    }
    stage ('docker image push') {
        sh 'make push'
    }
    stage('system tests') {
        withEnv(['SCRIPTS=https://raw.githubusercontent.com/EnMasseProject/travis-scripts/master']) {
            checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/EnMasseProject/systemtests.git']]]
            checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/EnMasseProject/enmasse.git']]]
            sh 'curl -s ${SCRIPTS}/run-tests.sh | bash /dev/stdin ""'
        }
    }
    stage('docker snapshot') {
        sh 'make snapshot'
    }
}
