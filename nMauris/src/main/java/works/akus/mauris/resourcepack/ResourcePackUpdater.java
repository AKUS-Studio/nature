package works.akus.mauris.resourcepack;

import works.akus.mauris.Mauris;
import works.akus.mauris.utils.HostFileUtil;
import works.akus.mauris.utils.files.MaurisDataConfig;

import java.io.File;

public class ResourcePackUpdater {

    Mauris mauris;
    GitHubAPI gitHubAPI;

    MaurisDataConfig dataFile;

    public ResourcePackUpdater(){
        this.mauris = Mauris.getInstance();
        this.gitHubAPI = new GitHubAPI(mauris.getConfig().getString("resource-pack-updater.github-token"));

        dataFile = new MaurisDataConfig(mauris, "data", "resourcepack");
    }

    public void setUp(){
        check();
        hostResourcePack();
    }

    public void stop(){
        hostingServer.stopServer();
    }

    File resourcePackFile;
    private void check(){
        //
        File dir = new File(mauris.getDataFolder() + File.separator + "resourcepack");
        dir.mkdirs();

        resourcePackFile = new File(dir + File.separator + "rp.zip");
        //

        GitRepository repo = gitHubAPI.getRepository("AKUS-Studio", "nature-resourcepack", "main");

        String newSha = repo.getLastCommitId();
        String savedSha = dataFile.getYaml().getString("last-commit", "");
        if(newSha.equals(savedSha)){
            mauris.getLogger().info("ResourcePack is up to date!");
            return;
        }

        mauris.getLogger().info("ResourcePack is old. Downloading a updated one!");
        //If new

        repo.downloadRepositoryAsZip(resourcePackFile);
        repo.removeBranchFolder(resourcePackFile);

        dataFile.getYaml().set("last-commit", newSha);
        dataFile.save();
    }


    HostFileUtil hostingServer;
    String resourcepackUrl;
    private void hostResourcePack(){
        mauris.getLogger().info("Hosting ResourcePack..");
        hostingServer = new HostFileUtil("localhost", 8090);
        resourcepackUrl = hostingServer.hostFile("resourcepack", resourcePackFile);
        mauris.getLogger().info("Hosted successfully resourcepack on " + resourcepackUrl);

    }




}
