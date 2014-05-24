package os2.main.hardware.HDD;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Arturas
 */
public class ExtMem {

    private static boolean initialized = false;
    private static final int sizeOfMemB = 65400; //in bytes, ~64 kB
    private static final int sizeOfMemInt = sizeOfMemB / 4; //in integers (words)
    private static final int rootSize = 80; //in integers
    private static final int FATSize = 959; //in integers

    private static int[] memory = new int[sizeOfMemInt];

    public static void print() {
        for (int i = 0; i < sizeOfMemInt; i++) {
            System.out.println(i + ": " + memory[i]);
        }
    }

    private static void setMem(int address, int value) {
        memory[address] = value;
    }

    private static int getMem(int address) {
        return memory[address];
    }

    static class FileSystem {

        public static int[] getFileList() {
            int[] list = new int[60];
            int j = 0;
            for (int i = 0; i < rootSize; i = i + 4) {
                if (getMem(i) != 0) {
                    list[j] = getMem(i);
                    list[j + 1] = getMem(i + 1);
                    list[j + 2] = getMem(i + 2);
                    j = j + 3;
                }
            }
            return list;
        }

        static void initialization() {
            if (initialized == false) {
                for (int i = 0; i <= sizeOfMemInt; i++) {
                    if ((i < rootSize) || ((i >= (rootSize + FATSize)) && i < sizeOfMemInt)) {
                        setMem(i, 0);
                    }
                    if (i >= rootSize && i < (rootSize + FATSize)) {
                        setMem(i, 1);
                        if (i == rootSize) {
                            setMem(i, -3);
                        }
                    }
                }
                initialized = true;
            }
        }

        private static boolean checkIfAlreadyTaken(int address) {
            int[] takenFATAddress = new int[20];
            int g = 0;
            for (int i = 3; i < rootSize; i = i + 4) {
                if (getMem(i) != 0) {
                    takenFATAddress[g] = getMem(i) + rootSize;
                    g++;
                }
            }

            for (int j = 0; j < takenFATAddress.length; j++) {
                if (address == takenFATAddress[j]) {
                    return true;
                }
            }
            return false;
        }

        private static int findEmptyFieldInFAT(int from) {
            for (int j = rootSize + 2; j < (rootSize + FATSize); j++) {
                if (getMem(j) == 1 && j != (rootSize + from) && !checkIfAlreadyTaken(j)) {
                    return j - (rootSize);
                }
            }
            return 0;
        }

        private static boolean checkIfFilenameAlreadyExists(int[] filename) {
            int[] tempFilenameAsInts = new int[3];
            for (int i = 0; i < rootSize; i = i + 4) {
                tempFilenameAsInts[0] = getMem(i);
                tempFilenameAsInts[1] = getMem(i + 1);
                tempFilenameAsInts[2] = getMem(i + 2);
                if (Utilities.compareArrays(tempFilenameAsInts, filename)) {
                    return true;
                }
            }
            return false;
        }

        private static int getLastFATentry(int fileNo) {
            int jump = getMem(fileNo * 4 + 3);
            if (getMem(rootSize + jump) == 1) {
                return jump;
            }
            int value = 0;
            boolean proceed = true;
            while (proceed) {
                value = getMem(rootSize + jump);
                if (value == -2) {
                    proceed = false;
                } else {
                    proceed = true;
                    jump = value;
                }
            }
            return jump;
        }

        static int initRoot(int[] filename) throws Exception {
            int fileNo = -1;
            boolean filenameExists = checkIfFilenameAlreadyExists(filename);
            if (filenameExists) {
                throw new Exception("Byla su tokiu pavadinimu jau egzistuoja!");
            }

            for (int i = 0; i < rootSize; i = i + 4) {
                if (getMem(i) == 0) {
                    setMem(i, filename[0]);
                    setMem(i + 1, filename[1]);
                    setMem(i + 2, filename[2]);
                    int emptySpace = findEmptyFieldInFAT(0);
                    if (emptySpace != 0) {
                        setMem(i + 3, emptySpace);
                        return fileNo = i / 4;
                    }
                }
            }
            return fileNo;
        }

        static int getFileNo(int[] filenameAsInts) {
            int[] tempFilenameAsInts = new int[3];
            for (int i = 0; i < rootSize; i = i + 4) {
                tempFilenameAsInts[0] = getMem(i);
                tempFilenameAsInts[1] = getMem(i + 1);
                tempFilenameAsInts[2] = getMem(i + 2);
                if (Utilities.compareArrays(tempFilenameAsInts, filenameAsInts)) {
                    return i / 4;
                }
            }
            return -1;
        }

        static void saveBlock(int[] blockOfData, int fileNo) throws UnsupportedEncodingException {
            int lastFATentry = getLastFATentry(fileNo);
            for (int i = 0; i < 16; i++) {
                setMem(rootSize + FATSize + lastFATentry * 16 + i, blockOfData[i]);
            }
            setMem(rootSize + lastFATentry, findEmptyFieldInFAT(lastFATentry));
            setMem(rootSize + findEmptyFieldInFAT(lastFATentry), -2);
        }

        static void closeSave(int fileNo) {
            int lastFATentry = getLastFATentry(fileNo);
            setMem(rootSize + lastFATentry, 0);
        }

        static int[] loadBlock(int number, int fileNo) {
            int[] block = new int[16];
            int jump = getMem(fileNo * 4 + 3);
            int value = 0;

            for (int i = 0; i <= number; i++) {
                value = getMem(rootSize + jump);
                if (i == number) {
                    for (int j = 0; j < 16; j++) {
                        block[j] = getMem(rootSize + FATSize + (i + 2) * 16 + j);
                    }
                } else {
                    jump = value;
                }
            }
            return block;
        }

        static boolean checkIfEOF(int number, int fileNo) {
            boolean end = false;
            int jump = getMem(fileNo * 4 + 3);
            int value = 0;

            for (int i = 0; i <= number + 1; i++) {
                value = getMem(rootSize + jump);
                if (i == number && value == 0) {
                    return end = true;
                } else {
                    jump = value;
                }
            }
            return end;
        }
    }
}
