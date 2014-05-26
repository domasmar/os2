package os2.main.hardware.HDD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arturas
 */
public class Serializator {

    private static String filename = "HDD";
    private static FileWriter writer;
    private static FileReader reader;

    public static void save(int[] memory, int size) {
        File file = new File(filename);
        file.delete();
        try {
            writer = new FileWriter(filename);
            for (int i = 0; i < size; i++) {
                writer.write(memory[i] + " ");
            }
        } catch (IOException e) {
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Serializator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static int[] load(int[] memory, int size) {
        try {
            reader = new FileReader(filename);
            String content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
            String arrayString[] = content.split("\\s+");
            for (int i = 0; i < arrayString.length; i++) {
                try {
                    memory[i] = Integer.parseInt(arrayString[i]);
                } catch (NumberFormatException nfe) {
                };
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Serializator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return memory;
    }
}
