
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;


public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String args[]) throws IOException {
        try {
            logger.addAppender(new FileAppender(new SimpleLayout(),"./link.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.setLevel(Level.ALL);
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/video",new VideoHandler());
        server.setExecutor(null);
        server.start();
    }

    static String scrap(){
        Document doc = null;
        Element video = null;
        String source = null;
        try{
            doc = Jsoup.connect("https://webtv.feratel.com/webtv/?cam=5132&design=v3&c0=0&c2=1&lg=en&s=0").userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0").get();
            video = doc.getElementById("fer_video");
            source = video.select("source").first().attr("src");
        }catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(source);
        return source;
    }

    static class VideoHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "<video controls autoplay>\n" +
                    "  <source src=\"" + scrap() +"\" type=\"video/mp4\">\n" +
                    "  Your browser does not support the video tag.\n" +
                    "</video>";
            Headers h = t.getResponseHeaders();
            h.set("Content-Type", "text/html");
            t.sendResponseHeaders(200,response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
