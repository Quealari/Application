import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class Application {

    public static void main(String[] args) throws IOException {
        var port = 8000;
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext context = httpServer.createContext("/api/hello", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "Hallo, world";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        });
        context.setAuthenticator(new BasicAuthenticator("myrealm") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals("admin") && pwd.equals("admin");
            }
        });
            httpServer.setExecutor(null);
            httpServer.start();
    }

}

/**
 curl -v -X GET localhost:8000/api/hello
 curl -v -X POST localhost:8000/api/hello
 curl -v localhost:8000/api/hello -H 'Authorization: Basic YWRtaW46YWRtaW4='
 */