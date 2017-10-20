#!/usr/bin/env groovy

pipeline {
  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '5', numToKeepStr: '15'))
  }
  agent none // TODO https://issues.jenkins-ci.org/browse/JENKINS-33510

  stages {
    stage('Build React app') {
      agent { docker 'alecharp/npm-build-tools' }
      steps {
        sh "npm i && npm build"
      }
      post {
        success {
          stash name: 'front', includes: 'target/generated-sources/front/*'
        }
      }
    }
    stage('Build String-Boot app') {
      agent { docker 'alecharp/maven-build-tools' } // TODO https://issues.jenkins-ci.org/browse/JENKINS-33510
      steps {
        script {
          def commitID = sh(returnStdout: true, script: 'git rev-parse --short --verify HEAD')?.trim()
          imageName = "alecharp/the-hive:${commitID}"
        }
        unstash 'front'
        configFileProvider([configFile(fileId: 'cloudbees-maven-settings', targetLocation: 'settings.xml')]) {
          sh 'mvn package -Dmaven.test.skip=true -s settings.xml'
        }
      }
      post {
        success {
          stash name: 'docker', includes: 'src/main/docker/*,target/the-hive.jar'
        }
      }
    }
    stage('Test') {
      agent { docker 'alecharp/maven-build-tools' } // TODO https://issues.jenkins-ci.org/browse/JENKINS-33510
      steps {
        configFileProvider([configFile(fileId: 'cloudbees-maven-settings', targetLocation: 'settings.xml')]) {
          sh 'mvn clean verify -s settings.xml -P coverage -Dfindbugs.failOnError=false'
        }
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/*-reports/*.xml'
          jacoco(classPattern: 'target/classes', execPattern: 'target/jacoco.exec', sourcePattern: 'src/main/java')
          findbugs(pattern: 'target/findbugsXml.xml')
        }
      }
    }
    stage('Docker') {
      agent { label 'docker' }
      when {
        anyOf { branch 'master'; branch 'develop' }
      }
      steps {
        unstash 'docker'
        sh "docker build -t ${imageName} -f src/main/docker/Dockerfile . && docker push ${imageName}"
      }
      post {
        success {
          archiveArtifacts artifacts: 'target/the-hive.jar', fingerprint: false
        }
      }
    }
  }
}
