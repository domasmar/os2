package os2.main.hardware.HDD;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Arturas
 */
public class FileSaver {

    private int fileNo;

    public FileSaver(int[] filenameAsInt) throws Exception {
        ExtMem.FileSystem.initialization();
        fileNo = ExtMem.FileSystem.initRoot(filenameAsInt);
        if (fileNo == -1) {
            throw new Exception("Nepavyko įrašyti failo! Galimai pasiektas maksimalus failų limitas!");
        }
    }

    public void saveBlockOfFile(int[] blockOfData) throws UnsupportedEncodingException {
        ExtMem.FileSystem.saveBlock(blockOfData, fileNo);
    }

    public void closeSavedFile() {
        ExtMem.FileSystem.closeSave(fileNo);
    }
}
