package works.akus.auth.auth.discord;

public class TokenInfo {

    public TokenInfo(String accessToken, String tokenType, String expires_in, String refreshToken, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expires_in = expires_in;
        this.refreshToken = refreshToken;
        this.scope = scope;
    }

    String accessToken;
    String tokenType;
    String expires_in;
    String refreshToken;
    String scope;


}
