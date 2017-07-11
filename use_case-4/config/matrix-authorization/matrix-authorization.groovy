import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
def env      = System.getenv()
GITHUB_ADMIN_USERS  = env['GITHUB_ADMIN_USERS']

// combine with GITHUB_ADMIN_USERS from secrets
def github_admin_users_sec = new File('/run/secrets/GITHUB_ADMIN_USERS')
if ( github_admin_users_sec.exists() ) {
  if (GITHUB_ADMIN_USERS != null) {
    GITHUB_ADMIN_USERS = GITHUB_ADMIN_USERS + ',' + github_admin_users_sec.text
  } else
  {
    GITHUB_ADMIN_USERS = github_admin_users_sec.text
  }
}
println('== github-authentication.groovy - GITHUB_ADMIN_USERS --> ' + GITHUB_ADMIN_USERS)

def strategy = new ProjectMatrixAuthorizationStrategy()
if (GITHUB_ADMIN_USERS != null && GITHUB_ADMIN_USERS.size() > 0) {
  def admins = GITHUB_ADMIN_USERS.split(',')
  admins.each() {
      strategy.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.Create"),it.trim())
      strategy.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.Delete"),it.trim())
      strategy.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains"),it.trim())
      strategy.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.Update"),it.trim())
      strategy.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.View"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Computer.Build"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Computer.Configure"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Computer.Connect"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Computer.Create"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Computer.Delete"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Computer.Disconnect"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Computer.Provision"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Hudson.Administer"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Hudson.ConfigureUpdateCenter"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Hudson.Read"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Hudson.RunScripts"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Hudson.UploadPlugins"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.Build"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.Cancel"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.Configure"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.Create"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.Delete"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.Discover"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.Move"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.Read"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.ViewStatus"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Item.Workspace"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Run.Delete"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Run.Replay"),it.trim())
      strategy.add(Permission.fromId("hudson.model.Run.Update"),it.trim())
      strategy.add(Permission.fromId("hudson.model.View.Configure"),it.trim())
      strategy.add(Permission.fromId("hudson.model.View.Create"),it.trim())
      strategy.add(Permission.fromId("hudson.model.View.Delete"),it.trim())
      strategy.add(Permission.fromId("hudson.model.View.Read"),it.trim())
      strategy.add(Permission.fromId("hudson.scm.SCM.Tag"),it.trim())
      strategy.add(Permission.fromId("jenkins.metrics.api.Metrics.HealthCheck"),it.trim())
      strategy.add(Permission.fromId("jenkins.metrics.api.Metrics.ThreadDump"),it.trim())
      strategy.add(Permission.fromId("jenkins.metrics.api.Metrics.View"),it.trim())
      strategy.add(Permission.fromId("org.jenkins.plugins.lockableresources.LockableResourcesManager.Reserve"),it.trim())
      strategy.add(Permission.fromId("org.jenkins.plugins.lockableresources.LockableResourcesManager.Unlock"),it.trim())
      println "======= Adding admin user: ${it.trim()}"
  }
}
strategy.add(Permission.fromId("hudson.model.Hudson.Read"),"anonymous")
//strategy.add(Permission.fromId("hudson.model.Item.Read"),"anonymous")
//strategy.add(Permission.fromId("hudson.model.Item.ViewStatus"),"anonymous")
strategy.add(Permission.fromId("hudson.model.View.Read"),"anonymous")
strategy.add(Permission.fromId("jenkins.metrics.api.Metrics.View"),"anonymous")
strategy.add(Permission.fromId("jenkins.metrics.api.Metrics.HealthCheck"),"anonymous")
instance.setAuthorizationStrategy(strategy)
println("== project-base-auth.groovy -> admins and anonymous rights configured.")

instance.save()
