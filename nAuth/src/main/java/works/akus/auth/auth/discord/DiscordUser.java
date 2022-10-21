package works.akus.auth.auth.discord;

public class DiscordUser {

    public DiscordUser(String id, String name, String discriminator) {
        this.id = id;
        this.name = name;
        this.discriminator = discriminator;
    }

    String id;
    String name;
    String discriminator;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscriminator() {
        return discriminator;
    }
}
