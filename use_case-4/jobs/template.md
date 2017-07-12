# Reference and help for creating/mastering your Jenkinfile

A Jenkinsfile is a text file that contains the definition of your Jenkins Pipeline, and is checked into your own github repository.

* [Jenkinsfile](https://jenkins.io/doc/book/pipeline/jenkinsfile) Official Documentation.
* Notifications example for [Jenkinsfile](https://jenkins.io/blog/2017/02/15/declarative-notifications/) to slack, hipchat & email
* Publishing HTML Reports [Jenkinsfile](https://jenkins.io/blog/2017/02/10/declarative-html-publisher/)
* All Options available in Jenkinsfiles - [Pipeline steps](https://jenkins.io/doc/pipeline/steps/)

## Jenkinsfile Template:
```
pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr:'10'))
        timestamps()
        ansiColor('xterm')
    }
    agent {
        label 'docker-in-docker'
    }
    stages {
        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo 'Building..'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
    post {
        success {
            logstashPush("SUCCESS")
        }
        failure {
            logstashPush("FAILURE")
        }
        unstable {
            logstashPush("UNSTABLE")
        }
        always {
            deleteDir()
        }
    }
}
```

## More Examples

### Mesosphere cli deployment
```
 stage ("deploy to mesosphere") {
   steps {
     script {
        def userInput = input(
         id: 'userInput', message: 'Api token dcos', parameters: [
         [$class: 'TextParameterDefinition', defaultValue: 'uat', description: 'Environment', name: 'env']
        ])
        wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: userInput, var: 'var1']]]) {
            docker.image('centos').inside {
              sh """
                  export http_proxy=http://proxy.yourdomain.net:8080
                  export https_proxy=http://proxy.yourdomain.et:8080
                  curl -O https://downloads.dcos.io/binaries/cli/linux/x86-64/dcos-1.9/dcos
                  mv dcos /usr/local/bin/
                  chmod +x /usr/local/bin/dcos
                  dcos config set core.dcos_url http://<MESOSPHERE_URL>
                  echo ${userInput} | dcos auth login
                  dcos marathon app add redis.json
              """
            }
        }
     }
  }
}
```
Note: _redis.json_  is the application json configuration file.

### Marathon API Deployment
```
stage ("deploy to marathon") {
   steps {
     script {
       sh "curl -i -H 'Content-Type: application/json' -d @redis.json http://<MARATHON_URL>/v2/apps"
     }
   }
}
```
Note: _redis.json_ is the application json configuration file.

### Get secrets from Vault
```
stage('Vault Global Function') {
  steps {
    script {
      ## Get Secrets
      def json = vaultGetSecrets()
      ## Use those secrets
      sh "docker login --username ${json.username} --password \"${json.password}\" hub.docker.hpecorp.net"
    }
  }
}
```

### Deploy container to UCP
```
stage('Deploy to UCP') {
  steps {
    script {
      /* Get credentials bundle first */
      ucpGetBundlePlane("UCP_USERNAME","UCP_PASSWORD","https://ucpurl.net")
      /* Deploy to UCP */
      ucpRunCmd "docker run -d MY_IMAGE"
    }
  }
}
```
Note: when you use password, make sure you use the password wrapper, so your credentials are not shown in the console output.

##### Using Password Wrapper, hide creds from output.
```
stage('hide passwords') {
  steps {
    script {
      def json = vaultGetSecrets()
      wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: json.password, var: 'var1']]]) {
        ucpGetBundlePlane("${json.username}","${json.password}","https://dashboard.yourdomain.net")
      }
    }
  }
}
```
