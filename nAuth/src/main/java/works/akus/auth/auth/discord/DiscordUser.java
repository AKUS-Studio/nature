package works.akus.auth.auth.discord;

public class DiscordUser {

    public DiscordUser(String id, String name, String discriminator, String email, TokenInfo info) {
        this.id = id;
        this.name = name;
        this.discriminator = discriminator;
        this.email = email;
        this.tokenInfo = info;
    }

    String id;
    String name;
    String discriminator;
    String email;

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

    public String getEmail() {
        return email;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }
}
