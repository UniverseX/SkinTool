package com.autoai.themechecker;

import com.autoai.themechecker.util.IOUtil;
import com.autoai.themechecker.util.Log;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataModelJpg implements DataModel {
    private String[] standardDiffArray;
    private String[] dstDiffArray;
    private String ignorePath;
    private String standardPath;
    private String dstPath;
    private List<String> standardPngs;

    public DataModelJpg(String ignorePath, String standardPath, String dstPath) {
        this.ignorePath = ignorePath;
        this.standardPath = standardPath;
        this.dstPath = dstPath;
    }

    public void prepareStandData(){
        //1.准备数据（标准主题png图片）名称
        List<String> ignores = /*getFileContentList(ignorePath)*/new ArrayList<>();
        List<String> standardFiles = getFileContentList(standardPath);
        if(standardFiles == null){
            return;
        }
        standardPngs = new ArrayList<>();
        for (String name : standardFiles) {
            if(ignores != null && ignores.contains(name)){
                continue;
            }
            if(name.endsWith(".png") || name.endsWith(".jpg")){
                standardPngs.add(name);
            }
        }

    }

    public void doCompare(String dstPath) {
        Log.println(standardPngs);
        //目标数据
        ArrayList<String> dstList = new ArrayList<>();
        IOUtil.fileNameList(dstPath, dstList, new IOUtil.Filter() {
            @Override
            public boolean filter(String str) {
                return str.endsWith(".png") || str.endsWith(".jpg");
            }
        });
        Log.println(dstList);

        //2.对比数据
        //标准主题未匹配List
        ArrayList<String> standardDiff = new ArrayList<>();

        for (String png : standardPngs) {
            if(!dstList.contains(png)){
                standardDiff.add(png);
            }
        }

        //目标主题多余图片List
        ArrayList<String> dstDiff = new ArrayList<>();
        for (String dstPng : dstList) {
            if(!standardPngs.contains(dstPng)){
                dstDiff.add(dstPng);
            }
        }

        if(standardDiff.isEmpty()){
            standardDiff.add("标准主题适配成功");
        }

        if(dstDiff.isEmpty()){
            dstDiff.add("目标主题没有多余文件");
        }
        standardDiffArray = standardDiff.toArray(new String[0]);
        dstDiffArray = dstDiff.toArray(new String[0]);

        Log.println("标准未匹配: " + Arrays.toString(standardDiffArray));
        Log.println("目标多余: " + Arrays.toString(dstDiffArray));
    }

    public String[] getStandardDiffArray() {
        return standardDiffArray;
    }

    public String[] getDstDiffArray() {
        return dstDiffArray;
    }

    private static List<String> getFileContentList(String path)  {
        try {
            File file = new File(path);
            return Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
