package works.akus.mauris.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class HostFileUtil {


    static HashMap<String, HostFileUtil> servers = new HashMap<>();

    HttpServer server;

    String rawUrl;

    String ip;
    int port;
    //TODO static String overrideUrl;

    public HostFileUtil(String ip, int port){
        this.ip = ip;
        this.port = port;

        this.rawUrl = ip + ":" + port;
        HostFileUtil util = servers.get(rawUrl);
        if(util != null) {
            throw new RuntimeException("Server is already running on " + rawUrl + " please make sure to stop server before creating a new one");
        }

        startServer();
    }

    private void startServer(){
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.setExecutor(Executors.newCachedThreadPool());

        server.start();
        servers.put(rawUrl, this);
    }

    public void stopServer(){
        server.stop(0);
        servers.remove(rawUrl);
    }

    public String hostFile(String context, File file){
        HHandler hHandler = new HHandler(file.getPath());

        server.createContext("/" + context, hHandler);
        String url = "http://" + ip + ":" + port + "/" + context;

        return url;
    }

    static class HHandler implements HttpHandler {

        String path;
        public HHandler(String path) {
            this.path = path;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            Headers h = t.getResponseHeaders();

            File newFile = new File(path);
            byte[] data = Files.readAllBytes(newFile.toPath());

            h.add("Content-Type", "application/" + getFilesExtension(newFile));
            t.sendResponseHeaders(200, data.length);
            OutputStream os = t.getResponseBody();
            os.write(data);
            os.close();
        }
    }

    public static String getFilesExtension(File file){
        String fileName = file.toString();

        int index = fileName.lastIndexOf('.');
        if(index > 0) {
            return fileName.substring(index + 1);
        }

        return "";
    }
}
