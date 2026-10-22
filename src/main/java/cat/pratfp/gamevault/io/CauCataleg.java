package cat.pratfp.gamevault.io;

import cat.pratfp.gamevault.model.Joc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Cau binària del catàleg: desa la llista sencera amb serialització Java per recuperar-la ràpidament.
 * Només la pot llegir Java: no és un format d'intercanvi.
 */
public class CauCataleg {

    /*
     * TODO PR1-11 · Serialitzar i desserialitzar el catàleg (CA 1.3 i 1.4)
     * desa:    escriu amb un ObjectOutputStream UN sol objecte: new ArrayList<>(jocs).
     *          (List.of(...) també és serialitzable, però així sempre desem el mateix tipus.)
     * carrega: llegeix l'objecte amb un ObjectInputStream i retorna'l com a List<Joc>.
     *          readObject pot llançar ClassNotFoundException: embolica-la en una IOException
     *          amb un missatge clar i la causa original.
     * Tots dos amb try-with-resources.
     * 2a part: perquè funcioni, el record Joc ha de ser serialitzable (fitxer Joc.java).
     * Pista: sessió 6, Ex1Serialitza.
     */
    public void desa(Path p, List<Joc> jocs) throws IOException {
        // Escriu aquí el teu codi (PR1-11)

        //S'obre el flux de sortida de fitxer amb Files.newOutputStream(p)
        //Després envia la llista convertida x assegurar un tipus concret
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(p))) {
            oos.writeObject(new ArrayList<>(jocs));
        }
    }

    /**
     * Recupera el catàleg desat amb {@link #desa(Path, List)}.
     *
     * @param p fitxer .ser
     * @return la llista de jocs recuperada
     * @throws IOException si el fitxer no existeix, està corromput o la classe no es troba
     */
    @SuppressWarnings("unchecked")
    public List<Joc> carrega(Path p) throws IOException {
        // Escriu aquí el teu codi (PR1-11)

        //S'obre el flux d'entrada, llegeix l'objecte
        //Si la classe no es troba, la captura i la torna a llançar reempaquetada en una excep
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(p))) {
            return (List<Joc>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("No s'ha trobat la classe al desserialitzar " + p.getFileName(), e);
        }
    }
}
