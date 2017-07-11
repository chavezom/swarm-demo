import org.jenkinsci.plugins.github_branch_source.*

ep = GitHubConfiguration.get()

def env = System.getenv()
GITHUB_API_URL = env['GITHUB_API_URL']

def listEndopints = [
  new Endpoint(GITHUB_API_URL,"GHE")
]

ep.setEndpoints(listEndopints)
