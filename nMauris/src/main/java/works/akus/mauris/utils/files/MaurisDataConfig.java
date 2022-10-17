package works.akus.mauris.utils.files;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MaurisDataConfig {

	private static final String FILE_EXTENSION = ".yml";
	
    private Plugin plugin;
    private String name;
    private String path;

    private File file;
    private YamlConfiguration yaml;

    public MaurisDataConfig(Plugin plugin, String name, String path){
        this.plugin = plugin;
        this.name = name.replace(FILE_EXTENSION, new String());
        this.path = path;

        load();
    }

    public void load(){
    	String path = File.separator;
        String name = this.name + FILE_EXTENSION;
        
        if (this.path.isEmpty()) {
        	path += this.path + File.separator;
        }
        
        file = new File(plugin.getDataFolder() + path + name);
        
        if(!file.exists()){
            file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public void save(){
        try {
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }
}
