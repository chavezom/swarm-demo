import jenkins.model.*
def env = System.getenv()
Jenkins.instance.setNumExecutors(env['JENKINS_EXECUTORS'].toInteger())
println("== executors.groovy --> " + env['JENKINS_EXECUTORS'])
