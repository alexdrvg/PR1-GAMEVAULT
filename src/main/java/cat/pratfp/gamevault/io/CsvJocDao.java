package cat.pratfp.gamevault.io;

import cat.pratfp.gamevault.model.Joc;
import cat.pratfp.gamevault.model.ResultatCarrega;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lectura i escriptura del catàleg en CSV amb OpenCSV (separador ; i UTF-8).
 */
public class CsvJocDao {

    /** Capçalera del fitxer CSV, en aquest ordre. */
    public static final String[] CAPCALERA =
            {"id", "titol", "plataforma", "estudi", "dataSortida", "horesJugades", "nota"};

    private static final char SEPARADOR = ';';

    /**
     * Llegeix un CSV de jocs sense aturar-se per les línies dolentes.
     *
     * @param p fitxer CSV amb capçalera
     * @return els jocs vàlids i una incidència per cada línia descartada
     * @throws IOException si el fitxer no es pot llegir o no és un CSV
     */
    public ResultatCarrega llegeix(Path p) throws IOException {
        List<Joc> jocs = new ArrayList<>();
        List<String> incidencies = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder().withSeparator(SEPARADOR).withQuoteChar('"').build();

        try (Reader r = Files.newBufferedReader(p, StandardCharsets.UTF_8);
             CSVReader csv = new CSVReaderBuilder(r).withCSVParser(parser).withSkipLines(1).build()) {

            String[] c;
            int linia = 1;                      // la línia 1 és la capçalera
            while ((c = csv.readNext()) != null) {
                linia++;
                /*
                 * TODO PR1-06 · Convertir una línia en un Joc sense petar (CA 1.3 i 1.6)
                 * c[] té els camps de la línia. Si la línia és bona, afegeix el Joc a jocs.
                 * Si no ho és, afegeix a incidencies un text "línia N: motiu" i CONTINUA amb la següent:
                 *   - menys de 7 camps                 -> "línia N: incompleta"
                 *   - data que no es pot convertir     -> "línia N: data mal formada"
                 *   - id, hores o nota no numèrics     -> "línia N: número invàlid"
                 *   - el Joc rebutja les dades         -> "línia N: " + el missatge de l'excepció
                 * Compte: NumberFormatException és filla d'IllegalArgumentException.
                 * Pista: sessió 5, Ex3LlegeixCsvBrut.
                 */
                // Escriu aquí el teu codi (PR1-06)
                if (c.length < 7) {
                    incidencies.add("línia " + linia + ": incompleta");
                    continue;
                }

                try {
                    jocs.add(new Joc(
                            Integer.parseInt(c[0].trim()),
                            c[1].trim(),
                            c[2].trim(),
                            c[3].trim(),
                            LocalDate.parse(c[4].trim()),
                            Double.parseDouble(c[5].trim()),
                            Double.parseDouble(c[6].trim())
                    ));
                } catch (DateTimeParseException e) {
                    incidencies.add("línia " + linia + ": data mal formada");
                } catch (NumberFormatException e) {
                    incidencies.add("línia " + linia + ": número invàlid");
                } catch (IllegalArgumentException e) {
                    incidencies.add("línia " + linia + ": " + e.getMessage());
                }
            }
        } catch (CsvValidationException e) {
            throw new IOException("CSV mal format a " + p, e);
        }
        return new ResultatCarrega(jocs, incidencies);
    }

    /*
     * TODO PR1-07 · Escriure el catàleg en CSV (CA 1.4)
     * Escriu el fitxer sencer (si ja existeix, se sobreescriu):
     *   - primera fila: CAPCALERA
     *   - una fila per joc: fes servir aCamps(j)
     * - CSVWriter amb separador SEPARADOR, dins d'un try-with-resources, i Writer en UTF-8.
     * - writeNext(fila, false): així només posa cometes quan cal (un títol amb ;).
     * Pista: sessió 5, Ex4EscriuCsv.
     */
    public void escriu(Path p, List<Joc> jocs) throws IOException {
        // Escriu aquí el teu codi (PR1-07)
        try (Writer w = Files.newBufferedWriter(p, StandardCharsets.UTF_8);
             CSVWriter csvWriter = new CSVWriter(
                     w,
                     SEPARADOR,
                     CSVWriter.DEFAULT_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {

            // Aquí s'escriu la capçalera
            csvWriter.writeNext(CAPCALERA, false);

            // Aquí s'escriu cada joc convertit a array de String
            for (Joc j : jocs) {
                csvWriter.writeNext(aCamps(j), false);
            }
        }
    }

    /**
     * Converteix un Joc en la fila de text que espera el CSV.
     *
     * @param j joc
     * @return els 7 camps en l'ordre de la capçalera
     */
    static String[] aCamps(Joc j) {
        return new String[]{String.valueOf(j.id()), j.titol(), j.plataforma(), j.estudi(),
                j.dataSortida().toString(), String.valueOf(j.horesJugades()), String.valueOf(j.nota())};
    }
}
