node {
    checkout scm
    sh 'git submodule update --init' 
    withEnv(['SCRIPTS=https://raw.githubusercontent.com/EnMasseProject/travis-scripts/master']) {
        stage ('build') {
            sh './build.sh'
        }
        stage ('docker image') {
            sh 'make'
        }
        stage('system tests') {
            sh 'env'
            sh 'curl -s ${SCRIPTS}/setup-tests.sh | bash /dev/stdin' 
        }
    }

}
