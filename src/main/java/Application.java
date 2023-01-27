import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class Application {

    public static void main(String[] args) throws IOException {
        var port = 8000;
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/api/test", exchange -> {
            String response = "Hallo";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();
            exchange.close();});
        httpServer.setExecutor(null);
        httpServer.start();

    }

}
