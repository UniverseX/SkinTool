package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class IOUtil {
    //复制方法
    public static void copy(String src, String des) throws Exception {
        //初始化文件复制
        File file1=new File(src);
        //把文件里面内容放进数组
        File[] fs=file1.listFiles();
        if(fs == null) {
            return;
        }
        //初始化文件粘贴
        File file2=new File(des);
        //判断是否有这个文件有不管没有创建
        if(!file2.exists()){
            file2.mkdirs();
        }
        //遍历文件及文件夹
        for (File f : fs) {
            if(f.isFile()){
                //文件
                fileCopy(f.getPath(),des+"\\"+f.getName()); //调用文件拷贝的方法
            }else if(f.isDirectory()){
                //文件夹
                copy(f.getPath(),des+"\\"+f.getName());//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
            }
        }

    }

    /**
     * 文件复制的具体方法
     */
    public static void fileCopy(String src, String des) throws Exception {
        //io流固定格式
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
        int i = -1;//记录获取长度
        byte[] bt = new byte[2014];//缓冲区
        while ((i = bis.read(bt))!=-1) {
            bos.write(bt, 0, i);
        }
        bis.close();
        bos.close();
        //关闭流
    }

    public static void fileNameList(String srcPath, List<String> nameList, Filter filter){
        File file = new File(srcPath);

        if(file.isDirectory()) {
            File[] files = file.listFiles();
            if(files == null){
                return;
            }
            for (File f : files) {
                fileNameList(f.getAbsolutePath(), nameList, filter);
            }
        }else {
            if(filter.filter(file.getName())) {
                nameList.add(file.getName());
            }
        }

    }

    public static void filePathList(String srcPath, List<String> pathList, Filter filter){
        File file = new File(srcPath);

        if(file.isDirectory()) {
            File[] files = file.listFiles();
            if(files == null){
                return;
            }
            for (File f : files) {
                filePathList(f.getAbsolutePath(), pathList, filter);
            }
        }else {
            if(filter.filter(file.getAbsolutePath())) {
                pathList.add(file.getAbsolutePath());
            }
        }

    }

    public static void writeStrListToFile(String dec, List<String> strs, Filter filter) throws Exception{
        StringBuffer sb = new StringBuffer();
        for (String str : strs) {
            if(filter.filter(str)) {
                sb.append(str + "\r\n");
            }
        }

        FileWriter fileWriter = new FileWriter(dec, true);//true 追加
        fileWriter.write(sb.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    public interface Filter {
        boolean filter(String str);
    }

    public static List<String> getFileContentList(String path)  {
        try {
            File file = new File(path);
            return Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
