package com.surfilter.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

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

    /**
     * 读取文件。并将信息装换为字符串返回。
     * @param Path
     * @return
     */
    public static String ReadFile(String Path){
        BufferedReader reader = null;
        String laststr = "";
        try{
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while((tempString = reader.readLine()) != null){
                laststr += tempString;
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }
    /**
     * 读取文件。并将信息装换为字符串返回。
     * @param Path
     * @return
     */
    public static List<Map<String,String> >  ReadFileList(String Path){

        int count =0;
        BufferedReader reader = null;
        //String laststr = "";

        List<Map<String,String>> list = new ArrayList<>();
        try{
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while((tempString = reader.readLine()) != null){
                Map<String,String> map = new HashMap<>(  );
                String[] strs = tempString.split( "-" );
                if (strs.length > 1) {
                    String country = strs[0].trim();
                    String capital = strs[1].trim();
                    if (capital.contains( "(" )) {
                        capital = capital.substring( 0, capital.indexOf( "(" ) );
                    }
                    map.put( country, capital );
                    list.add( map );

                }
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    /**
     * 读取文件。并将信息装换为字符串返回。
     * @param Path
     * @return
     */
    public static List<String>  ReadFileList1(String Path){
        int count =0;
        BufferedReader reader = null;
        //String laststr = "";

        List<String> list = new ArrayList<>();
        try{
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while((tempString = reader.readLine()) != null){
                    list.add( tempString );
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
