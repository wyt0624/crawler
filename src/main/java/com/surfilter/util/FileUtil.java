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

    public static void isDirctionary(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     *
     * @param content 保存的文本内容
     * @param fileName //保存的文件名称。
     */
    public static void  saveSnapshot(String content,String fileName){
        BufferedWriter bw = null;
     //   File file = new File(path + url.replace("\"","") + "_" + System.currentTimeMillis() + ".txt");
        File file = new File(fileName);
        File yesFile = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            if(!file.exists()) {
                file.createNewFile();
            }
            bw.write(content);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
