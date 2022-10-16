package works.akus.mauris.utils.files;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MaurisDataConfig {

    Plugin plugin;
    String name;
    String path;

    public MaurisDataConfig(Plugin plugin, String name, String path){
        this.plugin = plugin;
        this.name = name.replace(".yml", "");
        this.path = path;

        load();
    }

    File file;
    YamlConfiguration yaml;

    public void load(){

        String path = this.path.isEmpty() ? File.separator : File.separator + this.path + File.separator;
        String name = this.name + ".yml";

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
