package works.akus.auth.auth.discord;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class OAuthServer {

    public OAuthServer(int port, String ip, String context) {
        this.port = port;
        this.ip = ip;
        this.context = context;
    }

    int port;
    String ip;
    String context;

    HttpServer server;
    public void start(OAuthServerResponse response){
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        server.createContext("/" + context, new Handler(response));
    }

    public void stop(){
        server.stop(0);
    }


    static class Handler implements HttpHandler {

        OAuthServerResponse response;
        public Handler(OAuthServerResponse response){
            this.response = response;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {

            Map<String, String> params = queryToMap(t.getRequestURI().getQuery());

            String code = params.get("code");
            String state = params.get("state");

            boolean success = response.run(code, state);

            byte[] data = success ? "S".getBytes() : "F".getBytes();

            t.sendResponseHeaders(200, data.length);
            OutputStream os = t.getResponseBody();
            os.write(data);
            os.close();
        }

        public Map<String, String> queryToMap(String query) {
            if(query == null) {
                return null;
            }
            Map<String, String> result = new HashMap<>();
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                }else{
                    result.put(entry[0], "");
                }
            }
            return result;
        }
    }

}
