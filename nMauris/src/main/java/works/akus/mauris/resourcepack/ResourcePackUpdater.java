package works.akus.mauris.resourcepack;

import works.akus.mauris.Mauris;
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

    public void check(){

        GitRepository repo = gitHubAPI.getRepository("AKUS-Studio", "nature-resourcepack", "main");

        String newSha = repo.getLastCommitId();
        String savedSha = dataFile.getYaml().getString("last-commit", "");
        if(newSha.equals(savedSha)){
            mauris.getLogger().info("ResourcePack is up to date!");
            return;
        }

        mauris.getLogger().info("ResourcePack is old. Downloading a updated one!");
        //If new
        File file = new File(mauris.getDataFolder() + File.separator + "resourcepack");
        file.mkdirs();

        File resourcePackZip = new File(file + File.separator + "rp.zip");

        repo.downloadRepositoryAsZip(resourcePackZip);
        repo.removeBranchFolder(resourcePackZip);

        dataFile.getYaml().set("last-commit", newSha);
        dataFile.save();
    }



}
