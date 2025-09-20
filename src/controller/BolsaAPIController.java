
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.blueprints.model.Bolsa;

@RestController
@RequestMapping("/Bolsa")
public class BolsaAPIController{
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            Set<Bolsa> data = services.getAllBolsa(); 
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error obteniendo datos", ex);
            return new ResponseEntity<>("Error obteniendo datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{Information}")
    public ResponseEntity<?> getByInformation(@PathVariable String Information) {
        try {
            Set<Blueprint> data = services.getBolsaByInformation(Information);
            if (data == null || data.isEmpty()) {
                return new ResponseEntity<>("Informacion no encontrada: " + Information, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error consultando autor: " + author, ex);
            return new ResponseEntity<>("Error consultando autor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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


        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error actualizando la Bolsa", e);
            return new ResponseEntity<>("Error actualizando Bolsa", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}