node {
    deleteDir()
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
        withCredentials([usernamePassword(credentialsId: 'a9bc53ba-716c-45de-9d74-dd5d003f83c3', passwordVariable: 'DOCKER_PASSWD', usernameVariable: 'DOCKER_USER')]) {
            sh 'docker login -u $DOCKER_USER -p $DOCKER_PASSWD $DOCKER_REGISTRY'
            sh 'make push'
        }
    }
    stage('system tests') {
        withCredentials([usernamePassword(credentialsId: '8957fba6-7473-40f6-8593-efefa9e42251', passwordVariable: 'OPENSHIFT_PASSWD', usernameVariable: 'OPENSHIFT_USER')]) {
            withEnv(['SCRIPTS=https://raw.githubusercontent.com/EnMasseProject/travis-scripts/master']) {
                checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/EnMasseProject/systemtests.git']]]
                checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/EnMasseProject/enmasse.git']]]
                sh 'export OPENSHIFT_PROJECT=`echo $JOB_NAME | tr / -`; curl -s ${SCRIPTS}/run-tests.sh | bash /dev/stdin ""'
            }
        }
    }
    stage('docker snapshot') {
        sh 'make snapshot'
    }
}
