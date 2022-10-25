package works.akus.auth.auth.discord;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.tokens.Token;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class DiscordAPI {

    public static final String CLIENT_ID = "1032909655984328724";
    private static final String CLIENT_SECRET = "-J8uRLiaQJKfbWBIq-8HZzzwl-H5q6Us";

    public final static String SCOPES = "email%20identify";
    public final static String TOKEN_TYPE = "BEARER";

    public final static String REDIRECT_URI_NORMAL = "http://localhost:9090/auth";
    public final static String REDIRECT_URI = "http%3A%2F%2Flocalhost%3A9090%2Fauth";
    public final static String AUTH_LINK = "https://discord.com/oauth2/authorize?client_id=1032909655984328724&redirect_uri=" + REDIRECT_URI + "&response_type=code&scope=" + SCOPES + "&prompt=none";

    public static String getAuthLink(){
        return AUTH_LINK;
    }

    public static DiscordUser getDiscordUserByToken(TokenInfo info){
        JSONObject userObject = getDiscordUserInfo(info);

        String userId = (String) userObject.get("id");
        String username = (String) userObject.get("username");
        String discriminator = (String) userObject.get("discriminator");
        String email = (String) userObject.get("email");

        return new DiscordUser(userId, username, discriminator, email, info);
    }

    public static DiscordUser getDiscordUserByCode(String auth_code){
        JSONObject jsonToken = getJsonObjectToken(auth_code);
        TokenInfo info = new TokenInfo(
                (String) jsonToken.get("access_token"),
                (String) jsonToken.get("token_type"),
                jsonToken.get("expires_in").toString(),
                (String) jsonToken.get("refresh_token"),
                (String) jsonToken.get("scope"),
                new Date().getTime()
        );

        return getDiscordUserByToken(info);
    }

    public static JSONObject getDiscordUserInfo(TokenInfo info){
        String requestUrl = "https://discord.com/api/users/@me";

        try {
            URL url = new URL(requestUrl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("Authorization", "Bearer " + info.accessToken);
            con.setRequestProperty("Content-Type", "application/json");

            InputStream inputStream = con.getInputStream();
            String json = inputStreamToString(inputStream);

            JSONObject object = stringToJson(json);

            return object;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static JSONObject getJsonObjectToken(String code){

        try {

            String url = "https://discord.com/api/oauth2/token";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", CLIENT_ID);
            parameters.put("client_secret", CLIENT_SECRET);
            parameters.put("grant_type", "authorization_code");
            parameters.put("code", code);
            parameters.put("redirect_uri", REDIRECT_URI_NORMAL);

            String form = parameters.keySet().stream()
                    .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form)).build();

            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return stringToJson(response.body().toString());
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }

        return null;
    }

    private static JSONObject stringToJson(String string){
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static String inputStreamToString(InputStream inputStream){
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }

    private static JSONObject jsonFromInputStream(InputStream is){
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject)jsonParser.parse(
                    new InputStreamReader(is, "UTF-8"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

}
