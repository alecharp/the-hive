#!/usr/bin/env groovy

pipeline {
  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '5', numToKeepStr: '15'))
  }
  agent { docker 'alecharp/maven-build-tools' }

  stages {
    stage('Build') {
      steps {
        configFileProvider([configFile(fileId: 'cloudbees-maven-settings', targetLocation: 'settings.xml')]) {
          sh 'mvn clean package -Dmaven.test.skip=true -s settings.xml -Dfindbugs.failOnError=false'
        }
      }
      post {
        always {
          findbugs(pattern: 'target/findbugsXml.xml')
        }
      }
    }
    stage('Test') {
      steps {
        configFileProvider([configFile(fileId: 'cloudbees-maven-settings', targetLocation: 'settings.xml')]) {
          sh 'mvn clean verify -s settings.xml -P coverage'
        }
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/*-reports/*.xml'
          jacoco(classPattern: 'target/classes', execPattern: 'target/jacoco.exec', sourcePattern: 'src/main/java')
        }
      }
    }
  }

  post {
    success {
      archiveArtifacts artifacts: 'target/the-hive.jar', fingerprint: true
    }
  }
}
