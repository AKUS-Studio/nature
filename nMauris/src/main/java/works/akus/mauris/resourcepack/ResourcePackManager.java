package works.akus.mauris.resourcepack;

import java.io.File;

import works.akus.mauris.Mauris;
import works.akus.mauris.resourcepack.github.GitHubAPI;
import works.akus.mauris.resourcepack.github.GitRepository;
import works.akus.mauris.resourcepack.hosting.FileHostServer;
import works.akus.mauris.utils.HashCalculator;
import works.akus.mauris.utils.files.MaurisDataConfig;

public class ResourcePackManager {
	
	private static final String GITHUB_ORGANIZATION = "AKUS-Studio";
	private static final String GITHUB_REPOSITORY = "nature-resourcepack";
	private static final String GITHUB_BRANCH = "main";
	private static final String RESOURCEPACK_DIRECTORY_NAME = "resourcepack";
	private static final String RESOURCEPACK_FILENAME = "resourcepack.zip";
	
	private Mauris mauris;
	private GitHubAPI github;
	
	private MaurisDataConfig dataFile;
	private File resourcePackFile;
	private byte[] resourcePackHash;
	
	private FileHostServer server;
	private String resourcepackUrl;

	
	public ResourcePackManager() {
		this.mauris = Mauris.getInstance();
		this.github = new GitHubAPI(mauris.getConfig().getString("resource-pack-updater.github-token"));
		this.dataFile = new MaurisDataConfig(mauris, "data", "resourcepack");
	}

	public void setup() {
		checkAndUpdate();
		hostResourcePack();
	}

	public void stopServer() {
		server.stopServer();
	}

	private void checkAndUpdate() {

		final File directory = new File(mauris.getDataFolder() + File.separator + RESOURCEPACK_DIRECTORY_NAME);
		directory.mkdirs();

		resourcePackFile = new File(directory, RESOURCEPACK_FILENAME);

		GitRepository repository = github.getRepository(
				GITHUB_ORGANIZATION,
				GITHUB_REPOSITORY,
				GITHUB_BRANCH);

		String newSha = repository.getLastCommitId();
		String savedSha = dataFile.getYaml().getString("last-commit", "");
		resourcePackHash = (byte[]) dataFile.getYaml().get("hash");
		
		if (newSha.equals(savedSha)) {
			mauris.getLogger().info("ResourcePack is up to date!");
			return;
		}

		mauris.getLogger().info("ResourcePack is old. Downloading a updated one!");

		repository.downloadRepositoryAsZip(resourcePackFile);
		repository.removeBranchFolder(resourcePackFile);

		resourcePackHash = HashCalculator.calcSHA1Hash(resourcePackFile);

		updateLastCommit(newSha, resourcePackHash);
	}
	
	private void updateLastCommit(String commitSha, byte[] fileHash) {
		dataFile.getYaml().set("last-commit", commitSha);
		dataFile.getYaml().set("hash", fileHash);
		dataFile.save();
	}

	private void hostResourcePack() {
		mauris.getLogger().info("Hosting the resource pack..");
		
		this.server = new FileHostServer("localhost", 8090);
		this.resourcepackUrl = server.hostFile("resourcepack", resourcePackFile);
		
		mauris.getLogger().info("Hosted successfully resourcepack on " + resourcepackUrl);
	}

	public byte[] getResourcePackHash() {return resourcePackHash; }

	public Mauris getMauris() {
		return mauris;
	}

	public GitHubAPI getGithub() {
		return github;
	}

	public File getResourcePackFile() {
		return resourcePackFile;
	}

	public FileHostServer getServer() {
		return server;
	}

	public String getResourcepackUrl() {
		return resourcepackUrl;
	}
}
