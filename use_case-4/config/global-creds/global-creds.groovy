import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import hudson.plugins.sshslaves.*
import hudson.util.Secret
import jenkins.model.*
import org.apache.commons.fileupload.*
import org.apache.commons.fileupload.disk.*
import org.jenkinsci.plugins.plaincredentials.*
import org.jenkinsci.plugins.plaincredentials.impl.*

domain = Domain.global()
store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

def github_token_secret = new File('/run/secrets/GITHUB_TOKEN')
if ( github_token_secret.exists() ) {
  GITHUB_TOKEN = github_token_secret.text

  secretText = new StringCredentialsImpl(
    CredentialsScope.GLOBAL,
    "github-token",
    "This is the token used for Github Pull Request Builder plugin",
    Secret.fromString(GITHUB_TOKEN)
  )

  usernameAndPassword1 = new UsernamePasswordCredentialsImpl(
    CredentialsScope.GLOBAL,
    "github-creds",
    "Github User/Pass Credentials",
    'git',
    GITHUB_TOKEN
  )

  store.addCredentials(domain, secretText)
  println "== global-creds.groovy - Credential added successfully id: github-token"
  store.addCredentials(domain, usernameAndPassword1)
  println "== global-creds.groovy - Credential added successfully id: github-creds"
} else {

println "== GITHUB_TOKEN secrete does not exist, skipping configuration"

}
