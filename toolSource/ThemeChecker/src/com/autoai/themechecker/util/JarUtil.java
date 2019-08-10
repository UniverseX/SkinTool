package com.autoai.themechecker.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {

    public static List<JarEntry> getAllLocalJarEntry() {
        List<JarEntry> list = new ArrayList<>();
        try {
            @SuppressWarnings("resource")
            //获得jar包路径
                    JarFile jFile = new JarFile(System.getProperty("java.class.path"));
            Enumeration<JarEntry> jarEntrys = jFile.entries();
            while (jarEntrys.hasMoreElements()) {
                JarEntry entry = jarEntrys.nextElement();
                System.out.println(entry.getName());
                list.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
