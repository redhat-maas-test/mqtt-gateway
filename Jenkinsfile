node {
    checkout scm
    sh 'git submodule update --init' 
    withEnv(['SCRIPTS=https://raw.githubusercontent.com/EnMasseProject/travis-scripts/master']) {
        stage('setup system tests') {
            sh 'curl -s ${SCRIPTS}/setup-tests.sh | bash /dev/stdin' 
        }
        stage ('build') {
            sh './build.sh'
        }
        stage ('docker image') {
            sh 'make'
        }
    }
}
