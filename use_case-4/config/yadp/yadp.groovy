// Yet-Another-Plugin-initialization script
//

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.domains.Domain
import com.github.kostyasha.yad.DockerCloud
import com.github.kostyasha.yad.DockerConnector
import com.github.kostyasha.yad.DockerSlaveTemplate
import com.github.kostyasha.yad.credentials.DockerRegistryAuthCredentials
import com.github.kostyasha.yad.launcher.DockerComputerJNLPLauncher
import jenkins.model.Jenkins
import hudson.slaves.*
import org.jenkinsci.plugins.docker.commons.credentials.DockerServerCredentials
import java.nio.file.Files
import groovy.json.*
import static java.util.Collections.singletonList;

void log(String text) {
    println('== yadp.groovy - ' + text)
}

GroovyObject getVault() {
    // Loading vaultUtils.groovy
    scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent
    if (scriptDir == "/groovy") {
      scriptDir = env["JENKINS_HOME"] + "/init.groovy.d"
    }
    File sourceFile = new File("${scriptDir}/lib/vaultUtils.groovy");
    Class groovyClass = new GroovyClassLoader(getClass().getClassLoader()).parseClass(sourceFile);
    return (GroovyObject) groovyClass.newInstance();
}

String ensure_registry_auth(String cloud_name, String vault_path) {

    GroovyObject myVault = getVault()

    def json = myVault.getVaultSecrets("${vault_path}")
    if (!json) {
        log("${cloud_name} - Missing ${vault_path}. Requires 'docker_registry_credentials'.")
        return(null)
    }

    registry_auth_data = json.data.docker_registry_credentials
    // registry_auth_data = env["YADP_IMAGE_REGISTRY_CREDENTIALS"]

    if (!registry_auth_data) {
        log("${cloud_name} - No registry credential to manage. YADP_IMAGE_REGISTRY_CREDENTIALS is missing.")
        return (null)
    }

    registry_auth = registry_auth_data.split(":")

    if (registry_auth.size() != 5) {
        log("${cloud_name} - Unable to create registry credential. YADP_IMAGE_REGISTRY_CREDENTIALS must be formated as <credID>:<RegistryServer>:<User>:<Password>:<Email>")
        return (null)
    }

    cred_id = cloud_name + "-" + registry_auth[0]

    cred_provider = Jenkins.getInstance().getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0]
    creds = cred_provider.getCredentials()

    def auth = new DockerRegistryAuthCredentials(
            CredentialsScope.GLOBAL,
            cred_id,
            "Registry credentials for server '" + registry_auth[1] + "'",
            registry_auth[2],
            registry_auth[3],
            registry_auth[4]
    )

    def foundCred

    // Search for the credential
    List<Credentials> list = cred_provider.getDomainCredentialsMap().get(Domain.global());
    if (list.contains(auth)) {
        foundCred = list.get(list.indexOf(auth))
    }

    def store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
    // compare
    if (foundCred) {
        if (foundCred.getUsername() != auth.getUsername() ||
                foundCred.getPassword() != auth.getPassword() ||
                foundCred.getEmail() != auth.getEmail()) {
            store.updateCredentials(Domain.global(), foundCred, auth)
            log("${cloud_name} - Credential '" + cred_id + "' updated.")
        } else {
            log("${cloud_name} - No change on credential '" + cred_id + "'. Nothing done.")
        }
    } else {
        store.addCredentials(Domain.global(), auth)
        log("${cloud_name} - Credential '" + cred_id + "' added.")
    }

    return (cred_id)
}

String ensure_docker_client_certificate(String cloud_name, String vault_path) {

    GroovyObject myVault = getVault()

    def json = myVault.getVaultSecrets("${vault_path}")

    if (!json) {
        log("${cloud_name} - ${vault_path} is not defined. Unable to set docker client certificate " +
                "Please create it with 'client_key', 'client_certificate' and 'server_ca_certificate' data")
        return(null)
    }
    clientKey = json.data.client_key
    clientCertificate = json.data.client_certificate
    serverCaCertificate = json.data.server_ca_certificate
    if (!clientKey || !clientCertificate || !serverCaCertificate) {
        log("${cloud_name} - VAULT: client_key, client_certificate and server_ca_certificate must be set")
        return (null)
    }

    cred_id = cloud_name + "-yadp-docker-daemon-cred"
    cred_provider = Jenkins.getInstance().getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0]
    def foundCred
    creds = cred_provider.getCredentials()

    def auth = new DockerServerCredentials(
            CredentialsScope.GLOBAL,
            cred_id,
            "Docker daemon host certificate for " + cloud_name,
            clientKey,
            clientCertificate,
            serverCaCertificate
    )

    // Search for the credential
    List<Credentials> list = cred_provider.getDomainCredentialsMap().get(Domain.global());
    if (list.contains(auth)) {
        foundCred = list.get(list.indexOf(auth))
    }

    def store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
    // compare
    if (foundCred) {
        if (foundCred.getClientKey() != auth.getClientKey() ||
                foundCred.getClientCertificate() != auth.getClientCertificate() ||
                foundCred.getServerCaCertificate() != auth.getServerCaCertificate()) {
            store.updateCredentials(Domain.global(), foundCred, auth)
            log("${cloud_name} - Credential '" + cred_id + "' updated.")
        } else {
            log("${cloud_name} - No change on credential '" + cred_id + "'. Nothing done.")
        }
    } else {
        store.addCredentials(Domain.global(), auth)
        log("${cloud_name} - Credential '" + cred_id + "' added.")
    }
    return (cred_id)
}

Object Cloud_Validate(Object cloud) {

  if (!cloud.Docker_URL) {
    log('ERROR: Cloud NOT configured. Missing Docker server url.')
    return null
  }

  if (!cloud.Host_Credentials_vault_path) {
    log('ERROR: Cloud NOT configured. Missing Vault Path to Docker Host credentials.')
    return null
  }

  if (!cloud.Cloud_Name) {
    Random random = new Random()
    cloud.Cloud_Name = "docker-${random.nextInt(10 ** 5)}"
    log("${cloud.Cloud_Name} - WARNING: Docker cloud name is missing. random name as default.")
  }

  if (!cloud.Max_Containers || cloud.Max_Containers <= 0) {
    cloud.Max_Containers = 50
    log("${cloud.Cloud_Name} - WARNING: The Max Containers property is invalid. Must be an integer > 0. Used 50 as default.")
  }

  return (cloud)
}

Object Template_Validate(Object template) {

  if (!template.Docker_Image_Name) {
    log('ERROR: Docker template NOT configured. Missing Docker image.')
    return null
  }

  if (!template.Labels) {
    log('ERROR: Docker template NOT configured. Missing Labels for slave.')
    return null
  }

  if (!template.Remote_Filing_System_Root) {
    template.Remote_Filing_System_Root = "/home/jenkins"
    log("${template.Docker_Image_Name} - WARNING: The Remote Filing System Root is empty. '/home/jenkins' set as default.")
  }

  if (!template.Linux_user) {
    template.Linux_user = "jenkins"
    log("${template.Docker_Image_Name} - WARNING: The Linux user is empty. 'jenkins' set as default.")
  }

  if (!template.Max_Instances || template.Max_Instances <= 0) {
    template.Max_Instances = 10
    log("${template.Docker_Image_Name} - WARNING: The Max Instances property for image ${template.Docker_Image_Name}  is invalid. Must be an integer > 0. Used 10 as default.")
  }

  if (!template.Jenkins_url || template.Jenkins_url == "") {
    template.Jenkins_url = "default"
    log("${template.Docker_Image_Name} - WARNING: The Jenkins URL is empty. 'default' set as default.")
  }

  if (template.Ignore_certificate_check == "true") {
    template.Ignore_certificate_check = true
  } else if (template.Ignore_certificate_check == "false") {
    template.Ignore_certificate_check = false
  } else {
    template.Ignore_certificate_check = false
    log("${template.Docker_Image_Name} - WARNING: The check certificate parameter is empty or not valid. 'false' set as default.")
  }

  if (!template.Docker_Privileged_Mode || template.Docker_Privileged_Mode == "") {
    template.Docker_Privileged_Mode = false
    log("${template.Docker_Image_Name} - WARNING: The Docker Privileged mode is empty. 'false' set as default.")
  }

  return (template)
}

def env = System.getenv()
def cloud_list = []
def YADP_CONFIG_JSON  = env['YADP_CONFIG_JSON']
def payload = new URL(YADP_CONFIG_JSON).text
def slurper = new JsonSlurper()
def clouds = slurper.parseText(payload)

clouds.each {
  def docker_cloud = Cloud_Validate(it)
  if (!docker_cloud) {
    log("Docker Cloud could not be configured.")
    return
  }
  log("${docker_cloud.Cloud_Name} - Start configuration")

  def other_jenkins_url = env["YADP_LAUNCH_JNLP_JENKINS_URL"]
  def docker_network_mode = env["YADP_CREATE_NETWORK_MODE"]
  def master_container_id = env["HOSTNAME"]

  // Defining/Creating credentials in Jenkins.
  def creds_id = ensure_docker_client_certificate(docker_cloud.Cloud_Name, docker_cloud.Host_Credentials_vault_path)

  def FoundDockerCloud = Jenkins.getInstance().clouds.getByName(docker_cloud.Cloud_Name)

  DockerConnector connector = new DockerConnector(docker_cloud.Docker_URL)
  if (creds_id) {
      connector.setCredentialsId(creds_id)
  }

  // Get all the images configured for each of the clouds defined
  List<DockerSlaveTemplate> templates = new ArrayList<DockerSlaveTemplate>()

  docker_cloud.Images.each {
    def docker_cloud_template = Template_Validate(it)
    if (!docker_cloud_template) {
      log("${docker_cloud.Cloud_Name} - A Docker Template could not be configured.")
      return
    }

    DockerSlaveTemplate tmpl = new DockerSlaveTemplate()

    tmpl.setRemoteFs(docker_cloud_template.Remote_Filing_System_Root)

    launcher = tmpl.getLauncher()
    if (launcher instanceof DockerComputerJNLPLauncher) {
        launcher = (DockerComputerJNLPLauncher) launcher
    } else {
      launcher = new DockerComputerJNLPLauncher();
    }

    // If Jenkins_url is not defined or default in json, then use current jenkins master url
    if (other_jenkins_url && docker_cloud_template.Jenkins_url == "default") {
      launcher.setJenkinsUrl(other_jenkins_url)
    } else {
      launcher.setJenkinsUrl(docker_cloud_template.Jenkins_url)
    }

    launcher.setUser(docker_cloud_template.Linux_user)
    launcher.setLaunchTimeout(docker_cloud_template.Launch_timeout)
    launcher.setNoCertificateCheck(docker_cloud_template.Ignore_certificate_check)

    tmpl.setLauncher(launcher)

    tmpl.setLabelString(docker_cloud_template.Labels);

    dockerContainer = tmpl.getDockerContainerLifecycle()
    dockerContainer.setImage(docker_cloud_template.Docker_Image_Name)


    dockerCreateContainer = dockerContainer.getCreateContainer()

    // If Docker_Network_Mode is not defined or default in json, then use current jenkins master container id.
    if (docker_cloud_template.Docker_Network_Mode == "default") {
      dockerCreateContainer.setNetworkMode(docker_network_mode)
    } else {
      dockerCreateContainer.setNetworkMode(docker_cloud_template.Docker_Network_Mode)
    }

    dockerCreateContainer.setPrivileged(docker_cloud_template.Docker_Privileged_Mode)

    creds_id = ensure_registry_auth(docker_cloud.Cloud_Name, docker_cloud.Host_Credentials_vault_path)
    if (creds_id) {
        dockerContainer.getPullImage().setCredentialsId(creds_id)
    }

    if (docker_cloud_template.Env_Vars) {
        def entryList = []
        docker_cloud_template.Env_Vars.each {
          entryList.add(new EnvironmentVariablesNodeProperty.Entry(it.name,it.value))
          log("${docker_cloud.Cloud_Name} - Adding EnvVar - ${it.name}:${it.value}")
        }
        EnvironmentVariablesNodeProperty envPro = new EnvironmentVariablesNodeProperty(entryList);
        tmpl.setNodeProperties(singletonList(envPro))
    }

    //final EnvironmentVariablesNodeProperty.Entry entry = new EnvironmentVariablesNodeProperty.Entry("JENKINS_MASTER_ID", master_container_id);
    //final EnvironmentVariablesNodeProperty variablesNodeProperty = new EnvironmentVariablesNodeProperty(singletonList(entry));
    //tmpl.setNodeProperties(singletonList(variablesNodeProperty))

    templates.add(tmpl)
    log("${docker_cloud.Cloud_Name} - Image template '${docker_cloud_template.Labels}' added.")
  }

  dockerCloud = new DockerCloud(docker_cloud.Cloud_Name, templates, docker_cloud.Max_Containers, connector)
  cloud_list.add(dockerCloud)

  final jenkins = Jenkins.getInstance()
  if (!FoundDockerCloud) {
      jenkins.clouds.add(dockerCloud)
      log("${docker_cloud.Cloud_Name} - Cloud configured.")
  } else {
      if (!jenkins.clouds.contains(dockerCloud)) {
          // Jenkins.getInstance().clouds.set(dockerCloudIndex, dockerCloud)
          jenkins.clouds.remove(FoundDockerCloud)
          jenkins.clouds.add(dockerCloud)
          log("${docker_cloud.Cloud_Name} - Cloud updated.")
      } else {
          log("${docker_cloud.Cloud_Name} - No change to provide.")
      }
  }
  log("${docker_cloud.Cloud_Name} - Finish configuration")
}

final jenkins = Jenkins.getInstance()
jenkins.clouds.each {
   if (!cloud_list.contains(it)) {
     jenkins.clouds.remove(it)
     log("${it.name} - Removed Cloud")
   }
}
