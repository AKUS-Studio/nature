package works.akus.auth.auth.discord;

public class DiscordUser {

    public DiscordUser(String id, String name, String discriminator, TokenInfo info) {
        this.id = id;
        this.name = name;
        this.discriminator = discriminator;
        this.tokenInfo = info;
    }

    String id;
    String name;
    String discriminator;

    public void updateName(String name, String discriminator){
        this.name = name;
        this.discriminator = discriminator;
    }

    TokenInfo tokenInfo;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }
}
