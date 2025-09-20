
import java.net.URI; 
import java.net.http; 
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublishers; 
import java.time.Duration; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.concurrent;

public class ConcurrentClient {

    public static void main(String[] args) throws Exception {
        String base = args.length>0 ? args[0] : "http://localhost:8080"; int clients = args.length>1 ? Integer.parseInt(args[1]) : 200;
        HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .version(HttpClient.Version.HTTP_1_1).build();

        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>();

            for (int i = 0; i < clients; i++) {
        final int idx = i;
        CompletableFuture<HttpResponse<String>> f = CompletableFuture.supplyAsync(() -> {
            try {
                String payload = String.format(
                    "{\"Information\":\"Symbol\",\"Refreshed\":\"Interval\",\"Size\"Time}";)
                HttpRequest req = HttpRequest.newBuilder()
                    .POST(Bolsa.ofString(payload))
                    .build();
                return client.send(req, HttpResponse.ofString());
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, ex);
        futures.add(f);
    }
}