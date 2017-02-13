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
          sh 'mvn clean package -Dmaven.test.skip=true -s settings.xml'
        }
      }
      post {
        success {
          archiveArtifacts artifacts: 'target/the-hive.jar', fingerprint: true, onlyIfSuccessful: true
        }
      }
    }
    stage('Test') {
      steps {
        configFileProvider([configFile(fileId: 'cloudbees-maven-settings', targetLocation: 'settings.xml')]) {
          sh 'mvn clean verify -s settings.xml'
        }
      }
      post {
        success {
          junit allowEmptyResults: true, testResults: 'target/*-reports/*.xml'
          step([$class: 'JacocoPublisher', classPattern: 'target/classes', execPattern: 'target/**.exec', sourcePattern: 'src/main/java'])
        }
      }
    }
  }
}
