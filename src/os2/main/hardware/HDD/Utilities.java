package hdd;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author Arturas
 */
public class Utilities {

    public static int[] getFilenameAsInts(String filenameAsString) throws UnsupportedEncodingException, Exception {
        int[] filenameAsInts = new int[3];

        if (filenameAsString.length() <= 12) {
            byte[] bytesOfNameAscii = filenameAsString.getBytes("US-ASCII");
            byte[] bytesOfName = Arrays.copyOf(bytesOfNameAscii, 12);

            for (int i = 0; i < 3; i++) {
                filenameAsInts[i] = ByteBuffer.wrap(bytesOfName, i * 4, 4).getInt();
            }
        } else {
            throw new Exception("Failo pavadinimas per ilgas!");
        }

        return filenameAsInts;
    }

    public static boolean compareArrays(int[] array1, int[] array2) {
        boolean b = true;
        if (array1 != null && array2 != null) {
            if (array1.length != array2.length) {
                return b = false;
            } else {
                for (int i = 0; i < array2.length; i++) {
                    if (array2[i] != array1[i]) {
                        return b = false;
                    }
                }
            }
        } else {
            return b = false;
        }
        return b;
    }

    public static String getFilenameAsString(int[] filenameAsInts) throws UnsupportedEncodingException {
        String nameString = "";
        byte[] bytesAscii = new byte[12];

        System.arraycopy(ByteBuffer.allocate(4).putInt(filenameAsInts[0]).array(), 0, bytesAscii, 0, 4);
        System.arraycopy(ByteBuffer.allocate(4).putInt(filenameAsInts[1]).array(), 0, bytesAscii, 4, 4);
        System.arraycopy(ByteBuffer.allocate(4).putInt(filenameAsInts[2]).array(), 0, bytesAscii, 8, 4);

        nameString = new String(bytesAscii, "US-ASCII");
        nameString = nameString.trim();
        return nameString;
    }

    public static String getFileContentAsString(int[] contentAsInts) throws UnsupportedEncodingException {
        String content = "";
        byte[] bytesAscii = new byte[contentAsInts.length * 4];

        for (int i = 0; i < contentAsInts.length; i++) {
            System.arraycopy(ByteBuffer.allocate(4).putInt(contentAsInts[i]).array(), 0, bytesAscii, i * 4, 4);
        }
        content = new String(bytesAscii, "US-ASCII");

        return content;
    }

    public static int[] getFileContentsAsInts(String contentAsString) throws UnsupportedEncodingException {
        int sizeOfIntArray = contentAsString.length() / 4;
        if (contentAsString.length() % 4 != 0) {
            sizeOfIntArray++;
        }
        int[] contentInts = new int[sizeOfIntArray];

        byte[] bytesOfContentAscii = contentAsString.getBytes("US-ASCII");
        byte[] bytesOfContent = Arrays.copyOf(bytesOfContentAscii, sizeOfIntArray * 4);

        for (int i = 0; i < sizeOfIntArray; i++) {
            contentInts[i] = ByteBuffer.wrap(bytesOfContent, i * 4, 4).getInt();
        }

        return contentInts;
    }
}
