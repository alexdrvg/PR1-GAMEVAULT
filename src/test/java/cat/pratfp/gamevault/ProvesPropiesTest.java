package cat.pratfp.gamevault;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import cat.pratfp.gamevault.io.IndexHores;
import cat.pratfp.gamevault.model.Joc;
import cat.pratfp.gamevault.servei.CatalegException;
import cat.pratfp.gamevault.servei.Convertidor;
import java.time.LocalDate;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Les teves proves (PR1-14). Aquesta classe SÍ que l'has de modificar.
 * Escriu dues proves noves que no estiguin a les altres classes de prova.
 * Canvia el @DisplayName perquè digui què comproves i treu el fail().
 * Idees: l'índex amb un títol de més de 40 caràcters; una còpia de seguretat d'un fitxer sense extensió;
 * un CSV amb una línia en blanc; carregar un .json que no existeix amb el Convertidor;
 * sumaHores en dos registres seguits...
 */
class ProvesPropiesTest {

    @TempDir
    Path tmp;

    @Test
    @DisplayName("PR1-14 · la meva primera prova")
    void primeraProva() throws Exception {
        // Escriu aquí el teu codi (PR1-14)
        Convertidor convertidor = new Convertidor();
        Path fitxerInexistent = tmp.resolve("no_existeix.json");

        assertThrows(CatalegException.class, () -> {
            convertidor.carrega(fitxerInexistent);
        });
    }

    @Test
    @DisplayName("PR1-14 · la meva segona prova")
    void segonaProva() throws Exception {
        // Escriu aquí el teu codi (PR1-14)
        Path fitxerIndex = tmp.resolve("index.dat");
        IndexHores index = new IndexHores(fitxerIndex);

        Joc joc1 = new Joc(1, "Zelda", "Switch", "Nintendo", LocalDate.of(2017, 3, 3), 50.0, 9.8);
        Joc joc2 = new Joc(2, "Mario", "Switch", "Nintendo", LocalDate.of(2017, 10, 27), 20.0, 9.5);

        // Escrivim dos registres consecutius (posició 0 i posició 1)
        index.escriu(0, joc1);
        index.escriu(1, joc2);

        // Sumem hores a cadascun
        double novesHores1 = index.sumaHores(0, 10.5);
        double novesHores2 = index.sumaHores(1, 5.0);

        // Comprovem que les sumes s'han fet i aplicat de manera independent
        assertEquals(60.5, novesHores1, 0.001);
        assertEquals(25.0, novesHores2, 0.001);

        // Verifiquem que la lectura recupera els valors nous
        assertEquals(60.5, index.llegeixHores(0), 0.001);
        assertEquals(25.0, index.llegeixHores(1), 0.001);
    }

}
