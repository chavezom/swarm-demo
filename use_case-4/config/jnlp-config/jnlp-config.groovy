import jenkins.model.*

void log(String text) {
    println('== jnlp-config.groovy - ' + text)
}

def instance = Jenkins.getInstance()

jnlp_port = System.getenv("JNLP_PORT") ?: "50000"

instance.setSlaveAgentPort(jnlp_port.toInteger())
instance.save()
log("JNLP (${jnlp_port}) port configured.")
