node {
    checkout scm
    sh 'git submodule update --init' 
    stage ('build') {
        sh './build.sh'
    }
    stage ('docker image') {
        sh 'make'
    }
    stage('system tests') {
        sh 'echo Hello'
    }
}
