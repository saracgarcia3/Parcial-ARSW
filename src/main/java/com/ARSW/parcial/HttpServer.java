package com.ARSW.parcial;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiFunction;

public class HttpServer {

    private static Map<String, BiFunction<Request, Response, String>> getRoutes = new HashMap<>();

    public static void get(String path, BiFunction<Request, Response, String> handler) {
        getRoutes.put(path, handler);
    }

    private static String staticDir = "resources"; 

    public static void staticfiles(String folder) {
        staticDir = (folder != null && !folder.isEmpty()) ? folder : "resources";
    }

    public static void main(String[] args) throws IOException {
        int port = 8080; 

        
        staticfiles("resources");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor corriendo en http://localhost:" + port);

            
            get("/App/hello", (req, res) -> {
                List<String> names = req.getValues("name");
                if (names.isEmpty()) return "Hello world";
                return "Hello " + String.join(", ", names);
            });

            get("/App/pi", (req, res) -> String.valueOf(Math.PI));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String rawPath = tokens[1];

        
            String basePath = rawPath.split("\\?", 2)[0];

            Request req = new Request(method, rawPath);
            Response res = new Response();

            if ("GET".equalsIgnoreCase(method)) {
                BiFunction<Request, Response, String> handler = getRoutes.get(basePath);
                if (handler != null) {
                    String result = handler.apply(req, res);
                    res.setBody(result);
                    sendResponse(out, res);
                    return;
                }
            }

        
            serveStaticFile(basePath, out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendResponse(OutputStream out, Response res) throws IOException {
        byte[] bodyBytes = res.getBody().getBytes(StandardCharsets.UTF_8);
        String httpResponse = "HTTP/1.1 " + res.getStatus() + " OK\r\n" +
                              "Content-Type: text/plain; charset=UTF-8\r\n" +
                              "Content-Length: " + bodyBytes.length + "\r\n" +
                              "\r\n";
        out.write(httpResponse.getBytes(StandardCharsets.UTF_8));
        out.write(bodyBytes);
        out.flush();
    }

    private static void serveStaticFile(String path, OutputStream out) throws IOException {
        
        String safePath = path.startsWith("/") ? path.substring(1) : path;
        File file = new File(staticDir, safePath);

        if (!file.exists() || file.isDirectory()) {
            Response res = new Response();
            res.setStatus(404);
            res.setBody("404 Not Found");
            sendResponse(out, res);
            return;
        }

        byte[] content = Files.readAllBytes(file.toPath());
        String header = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n" + // simple; puedes mejorar si quieres detectar por extensión
                        "Content-Length: " + content.length + "\r\n" +
                        "\r\n";
        out.write(header.getBytes(StandardCharsets.UTF_8));
        out.write(content);
        out.flush();
    }
}