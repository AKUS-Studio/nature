package works.akus.mauris.resourcepack.hosting;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class FileHostServer {

	private HttpServer server;
	private String address;
	private int port;

	public FileHostServer(String address, int port) {
		this.address = address;
		this.port = port;
		startServer();
	}

	private void startServer() {
		try {
			server = HttpServer.create(new InetSocketAddress(port), 0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		server.setExecutor(Executors.newCachedThreadPool());

		server.start();
	}

	public void stopServer() {
		server.stop(0);
	}

	public String hostFile(String context, File file) {
		Handler hHandler = new Handler(file.getPath());

		server.createContext("/" + context, hHandler);
		String url = "http://" + address + ":" + port + "/" + context;

		return url;
	}

	static class Handler implements HttpHandler {

		private String path;

		public Handler(String path) {
			this.path = path;
		}

		@Override
		public void handle(HttpExchange t) throws IOException {
			Headers headers = t.getResponseHeaders();

			File newFile = new File(path);
			byte[] data = Files.readAllBytes(newFile.toPath());

			headers.add("Content-Type", "application/" + getFileExtension(newFile));
			t.sendResponseHeaders(200, data.length);
			OutputStream os = t.getResponseBody();
			os.write(data);
			os.close();
		}

		private String getFileExtension(File file) {
			final String fileName = file.toString();

			int index = fileName.lastIndexOf('.');
			if (index > 0) {
				return fileName.substring(index + 1);
			}

			return "";
		}
	}

}
