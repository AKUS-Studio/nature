package works.akus.mauris.resourcepack;

import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Logger;

public class GitHubAPI {

    String token;
    Logger logger;

    public GitHubAPI(String token){
        this.token = token;

        logger = Logger.getLogger("GitHub API");
    }

    public GitRepository getRepository(String owner, String repo, String branch){
        return new GitRepository(owner, repo, branch, this);
    }

}
