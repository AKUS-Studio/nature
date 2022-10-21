package works.akus.auth.auth.discord;

public interface OAuthServerResponse {

    boolean run(String code, String state);

}
