package com.surfilter.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Set;

public class FileUtil {

    /**
     * 文件拷贝
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    public static void writeKeyWordToFile(BufferedWriter bw, Set<Keyword> keywordSet) throws IOException {
        if (bw != null) {
            bw.write("\r\n");
            for (Keyword keyword : keywordSet) {
                if (keyword.getWord().length() > 1) {
                    bw.write(keyword.getWord() + " ");
                }
            }
        }
    }
}
