package cat.pratfp.gamevault.servei;

import cat.pratfp.gamevault.io.CsvJocDao;
import cat.pratfp.gamevault.io.JsonJocDao;
import cat.pratfp.gamevault.model.Joc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Convertidor de formats: un sol model intern (List&lt;Joc&gt;) i un DAO per format.
 * Convertir és llegir amb un DAO i escriure amb un altre.
 */
public class Convertidor {

    private final CsvJocDao csv = new CsvJocDao();
    private final JsonJocDao json = new JsonJocDao();

    /**
     * Carrega els jocs d'un fitxer triant el DAO per l'extensió (.csv o .json).
     * D'un CSV només es retornen els jocs vàlids.
     *
     * @param origen fitxer d'entrada
     * @return els jocs llegits
     * @throws CatalegException si el format no és suportat o el fitxer no es pot llegir
     */
    public List<Joc> carrega(Path origen) throws CatalegException {
        try {
            return switch (extensio(origen)) {
                case "csv" -> csv.llegeix(origen).jocs();
                case "json" -> json.llegeix(origen);
                default -> throw new CatalegException("Format no suportat: " + origen.getFileName());
            };
        } catch (IOException e) {
            throw new CatalegException("No s'ha pogut llegir " + origen.getFileName() + ": " + e.getMessage(), e);
        }
    }

    /*
     * TODO PR1-10 · Desar i convertir (CA 1.5 i 1.6)
     * desa: fes el mateix que carrega(), però escrivint:
     *   - "csv"  -> csv.escriu(desti, jocs)
     *   - "json" -> json.escriu(desti, jocs)
     *   - qualsevol altra extensió -> CatalegException amb el missatge "Format no suportat: <nom>"
     *   - si l'escriptura llança IOException -> CatalegException amb missatge clar i la causa.
     *   Compte: un switch amb fletxa que només executa mètodes void no retorna res; fes-lo com a sentència.
     * converteix: carrega l'origen, desa'l al destí i retorna quants jocs s'han convertit.
     */
    public void desa(Path desti, List<Joc> jocs) throws CatalegException {
        // Escriu aquí el teu codi (PR1-10)
        try {
            //Miren de quin tipus és l'extensió del fitxer de destí
            switch (extensio(desti)) {
                case "csv" -> csv.escriu(desti, jocs);
                case "json" -> json.escriu(desti, jocs);
                default -> throw new CatalegException("Format no suportat: " + desti.getFileName());
            }
            //Si no es cap dels dos casos, mandem missatge
        } catch (IOException e) {
            throw new CatalegException("No s'ha pogut escriure " + desti.getFileName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Converteix un fitxer d'un format a un altre.
     *
     * @param origen fitxer d'entrada (.csv o .json)
     * @param desti  fitxer de sortida (.csv o .json)
     * @return el nombre de jocs convertits
     * @throws CatalegException si algun dels dos formats no és suportat o hi ha un error d'E/S
     */
    public int converteix(Path origen, Path desti) throws CatalegException {
        // Escriu aquí el teu codi (PR1-10)

        // Carreguem els jocs del fitxer d'origen i els escrivim la llista amb el nou format, al fitxer de destí
        List<Joc> jocs = carrega(origen);
        desa(desti, jocs);

        // I retornem quants jocs s'han convertit
        return jocs.size();
    }

    /** Extensió del fitxer en minúscules i sense el punt ("" si no en té). */
    private static String extensio(Path p) {
        String nom = p.getFileName().toString();
        int punt = nom.lastIndexOf('.');
        return punt < 0 ? "" : nom.substring(punt + 1).toLowerCase(Locale.ROOT);
    }
}
