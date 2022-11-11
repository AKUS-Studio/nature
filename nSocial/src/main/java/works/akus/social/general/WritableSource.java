package works.akus.social.general;

import org.bukkit.configuration.ConfigurationSection;

public class WritableSource {

    protected ConfigurationSection sec;

    protected void setSec(ConfigurationSection sec) {
        this.sec = sec;
    }

    public void write(){}

}
