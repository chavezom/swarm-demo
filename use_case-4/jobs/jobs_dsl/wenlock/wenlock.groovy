organizationFolder('JenkinsHub') {
    authorization {
            permissionAll('wenlock')
            permission('hudson.model.Item.Read', 'anonymous')
            permission('hudson.model.Item.ViewStatus', 'anonymous')
         }
    organizations {
        github {
            apiUri('https://api.github.com')
            checkoutCredentialsId('github-creds')
            includes('*')
            repoOwner('wenlock')
            scanCredentialsId('github-creds')
            pattern('.*')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            numToKeep(20)
        }
    }
}
