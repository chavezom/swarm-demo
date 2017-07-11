
import jenkins.model.*
import hudson.util.Secret
import java.nio.file.Files
import groovy.json.JsonSlurper

class VaultUtils {
    def getVaultSecrets(vaultPath) {
        def command = "vault read -format=json ${vaultPath}"
        def credscmd = command.execute()
        credscmd.waitFor()
        if ( credscmd.exitValue() != 0 ) {
            println "== vaultSecrets function - Error, ${credscmd.err.text}"
        } else {
            // Parse the response
            def restResponse = """ ${credscmd.text} """
            def obj = new JsonSlurper().parseText( restResponse )
            return obj
        }
    }
}
