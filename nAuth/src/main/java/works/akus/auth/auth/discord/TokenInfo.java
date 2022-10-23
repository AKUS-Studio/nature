package works.akus.auth.auth.discord;

import java.util.Date;

public class TokenInfo {

    public TokenInfo(String accessToken, String tokenType, String expiresIn, String refreshToken, String scope, long takenAt) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.scope = scope;
        this.takenAt = takenAt;
    }

    String accessToken;
    String tokenType;
    String expiresIn;
    String refreshToken;
    String scope;
    long takenAt;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public long getTakenAt() {
        return takenAt;
    }
}
