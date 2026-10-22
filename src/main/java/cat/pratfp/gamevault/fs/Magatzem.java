package cat.pratfp.gamevault.fs;

import javax.xml.xpath.XPath;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

/**
 * El magatzem de fitxers de GameVault: una carpeta base amb tres subcarpetes, imp ex copies
 */
public class Magatzem {

    private static final DateTimeFormatter MARCA = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final Path base;

    /**
     * Crea un magatzem sobre una carpeta base. No toca el disc.
     *
     * @param base serà la carpeta arrel del magatzem (per exemple, Path.of("dades"))
     */
    public Magatzem(Path base) {

        this.base = base;
    }

    /** @return la carpeta d'importació (base/import) */
    public Path importacio() {

        return base.resolve("import");
    }

    /** @return la carpeta d'exportació (base/export) */
    public Path exportacio() {

        return base.resolve("export");
    }

    /** @return la carpeta de còpies de seguretat (base/backup) */
    public Path copies() {

        return base.resolve("backup");
    }


    public void preparaEstructura() throws IOException {
        // Escriu aquí el teu CODI (TODO PR1-02)
        Files.createDirectories(importacio());
        Files.createDirectories(exportacio());
        Files.createDirectories(copies());
    }

    /*
     * TODO PR1-03 · Còpia de seguretat amb marca de temps (CA 1.1)
     * Copia el fitxer dins de copies() amb el nom  <nom>_<marca>.<extensió>
     *   exemple: catalog.csv  ->  backup/catalog_20261013-151002.csv
     * - La marca te la dona marcaDeTemps().
     * - Separa el nom de l'extensió amb lastIndexOf('.').
     * - Si el destí ja existeix, l'ha de substituir.
     * - L'original NO es pot moure ni esborrar.
     * Retorna la ruta de la còpia.
     */
    public Path copiaSeguretat(Path fitxer) throws IOException {
        // Escriu aquí el teu codi (PR1-03)

        // primer aconseguim el nom del fitxer
        String nomFitxer = fitxer.getFileName().toString();

        //Després demanen el lloc en el qual està el . amb el seu índex
        int puntIndex = nomFitxer.lastIndexOf('.');

        String nom;
        String terminacio;

        if (puntIndex!=-1) { //si int ha trobat on pot estar l'índex, separa el nom y la terminacio: catalog + csv
            nom = nomFitxer.substring(0,puntIndex);
            terminacio = nomFitxer.substring(puntIndex);
        } else { // si dona error i el fitxer no té cap terminacio es queda igual
            nom = nomFitxer;
            terminacio="";
        }
        //ho guardem tot ha path que ho posarà en la subcarpeta copies
        Path destinacio = copies().resolve(nom+"_"+marcaDeTemps()+terminacio);

        //si el destí ja existeix, sobreescriu
        Files.copy(fitxer,destinacio, StandardCopyOption.REPLACE_EXISTING);

        return destinacio;
    }

    /*
     * TODO PR1-04 · Mida total del magatzem (CA 1.1)
     * Retorna la suma de les mides (en bytes) de TOTS els fitxers regulars que hi ha dins de la
     * carpeta base, també els de les subcarpetes. Les carpetes no compten.
     * Pista: sessió 2, Ex5Recorre. Files.walk obre recursos del sistema: va dins d'un try-with-resources.
     */
    public long midaTotal() throws IOException {
        // Escriu aquí el teu codi (PR1-04)

        try (Stream<Path> arbre = Files.walk(base)) {
            return arbre.filter(Files::isRegularFile)
                    .mapToLong(Magatzem::mida)
                    .sum();
        }
    }

    /**
     * Llista els fitxers regulars del magatzem amb la seva mida, ordenats per ruta.
     *
     * @return una línia per fitxer, amb el format "ruta relativa (N bytes)"
     * @throws IOException si no es pot recórrer la carpeta
     */
    public List<String> inventari() throws IOException {
        try (Stream<Path> camins = Files.walk(base)) {
            return camins.filter(Files::isRegularFile)
                    .sorted()
                    .map(p -> base.relativize(p) + " (" + mida(p) + " bytes)")
                    .toList();
        }
    }

    /** Mida d'un fitxer sense excepció comprovada, per poder-la fer servir dins d'un map. */
    private static long mida(Path p) {
        try {
            return Files.size(p);
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * Marca de temps per als noms de les còpies.
     *
     * @return la data i hora actuals amb el format yyyyMMdd-HHmmss
     */
    public static String marcaDeTemps() {
        return LocalDateTime.now().format(MARCA);
    }
}
