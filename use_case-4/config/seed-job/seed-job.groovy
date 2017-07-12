import jenkins.model.Jenkins
import hudson.model.FreeStyleProject
import hudson.plugins.git.GitSCM
import hudson.plugins.git.UserRemoteConfig
import hudson.plugins.git.BranchSpec
import hudson.plugins.git.SubmoduleConfig
import hudson.plugins.git.extensions.impl.PathRestriction
import hudson.model.Cause
import hudson.model.Cause.UserIdCause

import javaposse.jobdsl.plugin.*

import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*

def env = System.getenv()
seed_jobs_repo = env['SEED_JOBS_REPO']

// Lets get the git_password to checkout the seed repo
git_password = env['GIT_PASSWORD']
def secret_git_password = new File('/run/secrets/SEED_JOBS_GIT_PASSWORD')
if ( secret_git_password.exists() ) {
  git_password = secret_git_password.text
  println('== seed-job.groovy - SEED_JOBS_GIT_PASSWORD from docker secret')
}
git_username = env['GIT_USERNAME']


// A new line seperated list of dsl scripts, located in the workspace.
// Can use wildcards...and will be defaulted to jobs/**/*.groovy.
build_dsl_scripts = env['BUILD_DSL_SCRIPTS']

if( seed_jobs_repo ) {
  def credential_id
  def seed_jobs_repo_dir = new File(seed_jobs_repo)

  println("== seed-job.groovy --> SEED_JOBS_REPO is set to '" + seed_jobs_repo + "'")
  // Lets create the seed-job freesytle job if it's missing
  if(!Jenkins.instance.getItemMap().containsKey("seed-job")) {
    def seedJob = Jenkins.instance.createProject(FreeStyleProject.class, "seed-job")
    println("== seed-job.groovy --> FreestyleProject 'seed-job' created.")

    // We only need the credential if seed_jobs_repo is defined
    // and if the folder doesn't exist
    // otherwise setup the credentials so we can perform a checkout
    if (git_username && git_password && !seed_jobs_repo_dir.exists()) {
       println("== seed-job.groovy --> GIT_USERNAME is set to '" + git_username + "'")
       println("== seed-job.groovy --> GIT_PASSWORD is set to '***'")
       username_matcher = CredentialsMatchers.withUsername(git_username)
       available_credentials =
         CredentialsProvider.lookupCredentials(
           StandardUsernameCredentials.class,
           Jenkins.getInstance(),
           hudson.security.ACL.SYSTEM,
           new SchemeRequirement("ssh")
         )

       existing_credentials =
         CredentialsMatchers.firstOrNull(
           available_credentials,
           username_matcher
         )

       if (existing_credentials == null) {
          existing_credentials = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, 'github', 'GitHub pull request integration',
                                                                  git_username, git_password)
          global_domain = Domain.global()
          credentials_store =
          Jenkins.instance.getExtensionList(
               'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
             )[0].getStore()
          credentials_store.addCredentials(global_domain, existing_credentials)
          println("== seed-job.groovy --> 'github' credential added ")
      }
      credential_id = existing_credentials.id
    }
    else {
       println("== seed-job.groovy --> No credential to create/maintain as GIT_USERNAME or GIT_PASSWORD not set.")
    }
    if (seed_jobs_repo_dir.exists()){
      seed_jobs_repo = "file://" + seed_jobs_repo
    }
    def userRemoteConfig = new UserRemoteConfig(seed_jobs_repo, null, null, credential_id)

    def scm = new GitSCM(
      Collections.singletonList(userRemoteConfig),
      Collections.singletonList(new BranchSpec("master")),
      false,
      Collections.<SubmoduleConfig>emptyList(),
      null,
      null,
      null)

    if (!build_dsl_scripts) {
       build_dsl_scripts = "jobs_dsl/**/*.groovy"
       println("== seed-job.groovy --> BUILD_DSL_SCRIPTS not set. Using '" + build_dsl_scripts + "' as default")
    }
    else
       println("== seed-job.groovy --> Using '" + build_dsl_scripts + "' as build dsl scripts.")

    seedJob.scm = scm

    def scriptLocation = new ExecuteDslScripts()
    scriptLocation.setTargets(build_dsl_scripts)
    scriptLocation.setRemovedJobAction(RemovedJobAction.DISABLE)
    scriptLocation.setRemovedViewAction(RemovedViewAction.DELETE)
    scriptLocation.setLookupStrategy(LookupStrategy.JENKINS_ROOT)
    seedJob.buildersList.add(scriptLocation)
    println("== seed-job.groovy --> 'seed-job' configured. ")

    seedJob.save()
    seedJob.scheduleBuild(new Cause.UserIdCause())
  }
}
else
  println("== seed-job.groovy --> Missing SEED_JOBS_REPO, GIT_USERNAME, GIT_PASSWORD. 'seed-job' initial project NOT verified.")
