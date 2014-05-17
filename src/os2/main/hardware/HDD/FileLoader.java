package os2.main.hardware.HDD;

/**
 *
 * @author Arturas
 */
public class FileLoader {

    private int fileNo;
    private int loadedBlocks = 0;

    public FileLoader(int[] filenameAsInt) throws Exception {
        ExtMem.FileSystem.initialization();
        fileNo = ExtMem.FileSystem.getFileNo(filenameAsInt);
        if (fileNo == -1) {
            throw new Exception("Tokio failo nėra");
        }

    }

    public int[] getBlockOfFile() {
        int[] blockOfData = ExtMem.FileSystem.loadBlock(loadedBlocks, fileNo);
        loadedBlocks++;
        return blockOfData;
    }

    public boolean checkIfFileEnd() {
        boolean end = false;
        end = ExtMem.FileSystem.checkIfEOF(loadedBlocks, fileNo);
        return end;
    }

    public void closeLoadedFile() {
        loadedBlocks = 0;
    }
}