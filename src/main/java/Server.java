import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class Server {
    static MockServerClient mock = startClientAndServer(8085);
    public static void consulta(String method,String path,int statusCode,String content,String body,long delay) {

        mock.when(
                request()
                        .withMethod(method)
                        .withPath(path)
        ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader(new Header("Content-Type",content))
                        .withBody(body)
                        .withDelay(new Delay(TimeUnit.MILLISECONDS,(delay)))
        );

    }

    public static void main(String[] args) throws FileNotFoundException {

        Gson gson = new Gson();

        JsonReader reader = new JsonReader(new FileReader(System.getProperty("user.dir") + "/src/main/java/sites.json")); //cambiar path a gusto
        Sites[] sites = gson.fromJson(reader, Sites[].class);

        JsonReader reader2 = new JsonReader(new FileReader(System.getProperty("user.dir") + "/src/main/java/categories.json")); //cambiar path a gusto
        Categories[] categories = gson.fromJson(reader2, Categories[].class);

        consulta("GET","/users/sites",200,"application/json", new Gson().toJsonTree(sites).toString(),500);
        consulta("POST","/users",200,"application/json", "{\"username\":\"adriansuero\",\"token\":\"akjsdbakjda\"}",500);
        consulta("GET","/users/sites/.*/categories",200,"application/json",new Gson().toJsonTree(categories).toString(),500);

    }
}
