package cat.pratfp.gamevault.io;

import cat.pratfp.gamevault.model.Joc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Lectura i escriptura del catàleg en JSON amb Jackson.
 */
public class JsonJocDao {

    private final ObjectMapper mapper = creaMapper();

    /*
     * TODO PR1-08 (1a part) · Configurar l'ObjectMapper (CA 1.5)
     * Retorna un ObjectMapper que:
     *   - entengui LocalDate (registra el JavaTimeModule),
     *   - escrigui les dates com "2017-02-24" i no com [2017,2,24],
     *   - no peti si el JSON porta camps que Joc no té.
     * Pista: sessió 6, Ex4JsonAmbJackson.
     */

    private static ObjectMapper creaMapper() {
        // Escriu aquí el teu codi (PR1-08)
        //Creem primer els objectes
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Les dates com text i no com array
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Així no peta si el JSON porta camps que la classe joc no té
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper;

    }

    /*
     * TODO PR1-08 (2a part) · Escriure la llista en JSON (CA 1.4)
     * Escriu la llista sencera al fitxer, amb la sortida INDENTADA (una dada per línia).
     */
    public void escriu(Path p, List<Joc> jocs) throws IOException {
        // Escriu aquí el teu codi (PR1-08)

        //Activem el format d'impressió millorat, i converteix llista jocs en format JSON i l'escrivim al fitxer
        mapper.writerWithDefaultPrettyPrinter().writeValue(p.toFile(), jocs);
    }

    /*
     * TODO PR1-09 · Llegir la llista des de JSON (CA 1.3)
     * Llegeix el fitxer i retorna una List<Joc>.
     * Compte: amb List.class Jackson no sap que els elements són Joc. Què feia servir l'exemple?
     */
    public List<Joc> llegeix(Path p) throws IOException {
        // Escriu aquí el teu codi (PR1-09)
        throw new UnsupportedOperationException("PR1-09: encara no està fet");
    }
}
