# ​ 📚 Parcial – ARSW 

- En este parcial se nos pidio a construir una aplicación que demuestre una simple arquitectura distribuida web soportada en servicios REST

Para esto se implementaron las siguientes clases: 

- La clase **Bolsa().java** permite que como en el ejemplo de salida se puedan ver los requerimientos:
<p align="center"> 
<img width="656" height="122" alt="image" src="https://github.com/user-attachments/assets/6df064bd-1bf6-4a64-b69d-0a070f3fd166" />
</p>

```java
package com.ARSW.parcial;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Bolsa {
    private String Information;
    private String symbol;
    private String Refreshed;
    private String Interval;
    private String  Size;
    private String Time;
    private List<Point> points = new ArrayList<>();

    public Bolsa() { }

    public Bolsa(String Information, String symbol, String Refreshed, String Interval, String Size, String Time, Point[] pts) {
        this.Information = Information;
        this.symbol = symbol;
        this.Refreshed = Refreshed;
        this.Interval = Interval;
        this.Size = Size;
        this.Time = Time;

        if (pts != null) {
            points.addAll(Arrays.asList(pts));
        }
    }
```

- Seguimos con la clase **BolsaAPIController().java** en la cual podmos encontrar los servicios de **REST()**, tenemos, **GET()**, **POST()** y **PUT()**:
```java
@GetMapping("/{Information}/{symbol}")
    public ResponseEntity<?> getOne(@PathVariable String Informacion, @PathVariable String bpsymbol) {
        try {
            Bolsa bp = services.getBolsa(Informacion, bpsymbol);
            if (bp == null) {
                return new ResponseEntity<>("Informacion no encontrada: " + Information + "/" + bpsymbol,
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(bp, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error consultando en bolsa: " + Information + "/" + bpsymbol, ex);
            return new ResponseEntity<>("Error consultando bolsa", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

@PostMapping
    public ResponseEntity<?> create(@RequestBody Bolsa bp) {
        try {
            if (bp == null || bp.getInformation() == null || bp.getSymbol() == null) || bp.getRefreshed() == null || bp.getInterval() == null || bp.getSize() == null || bp.Time() == null{
                return new ResponseEntity<>("Body inválido: requiere mas valores",
                        HttpStatus.BAD_REQUEST);
            }
            services.createBolsa(bp);               
            return new ResponseEntity<>(bp, HttpStatus.CREATED);
    
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creando entidad Bolsa", e);
            return new ResponseEntity<>("Error creando entidad bolsa", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

@PutMapping("/{Information}/{bSymbol} /{Refreshed}/{Interval}/{Size}/{Time}")
    public ResponseEntity<?> updatePoints(@PathVariable String Information,
                                          @PathVariable String bSymbol,
                                          @PathVariable String Refreshed,
                                          @PathVariable String Interval,
                                          @PathVariable String Size,
                                          @PathVariable String Time,
                                          @RequestBody Blueprint payload) {
        try {
            if (payload == null || payload.getPoints() == null) {
                return new ResponseEntity<>("Body debe incluir 'points'", HttpStatus.BAD_REQUEST);
            }

            services.updateBolsaPoints(Information, bSymbol, Refreshed, Interval, Size, Time, payload.getPoints());
            Blueprint updated = services.getBolsa(Information, bSymbol, Refreshed, Interval, Size, Time); 
            return new ResponseEntity<>(updated, HttpStatus.OK);


```
 - Despues tenemos la clase del cliente concurrente, se intento plantear con otras dos clases de ayuda, **Request()** y **Response()** para su implementacion:

```java
package com.ARSW.parcial;

import java.net.http.HttpClient;
import java.time.Duration; 
import java.util.ArrayList; 
import java.util.List; 

public class ConcurrentClient {

    public static void main(String[] args) throws Exception {
        String base = args.length>0 ? args[0] : "http://localhost:8080"; int clients = args.length>1 ? Integer.parseInt(args[1]) : 200;
        HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .version(HttpClient.Version.HTTP_1_1).build();

        List<Response<String>> futures = new ArrayList<>();

            for (int i = 0; i < clients; i++) {
        final int idx = i;
        CompletableFuture<Response<String>> f = CompletableFuture.supplyAsync(() -> {
            try {
                String payload = String.format(
                    "{\"Information\":\"Symbol\",\"Refreshed\":\"Interval\",\"Size\"Time}";)
                Request req = Request.newBuilder()
                    .POST(Bolsa.ofString(payload))
                    .build();
                return client.send(req, Response);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, ex);
        futures.add(f);
    }
}
```
- Para compilar el proyecto usamos:
  
```java
mvn clean package -DskipTests
```
<p align="center"> 
<img width="975" height="401" alt="image" src="https://github.com/user-attachments/assets/daafae16-1425-438a-bd62-ecbe79686be0" />
</p>

- Y lo podemos iniciar con:

- Para ejecutarlo:
  
```java
mvn spring-boot:run
```
<p align="center"> 
<img width="805" height="428" alt="image" src="https://github.com/user-attachments/assets/26c6312f-cfb5-42e4-8030-86e1a859cf77" />
</p>

  
