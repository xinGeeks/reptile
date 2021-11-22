package com.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package java-reptile
 * @Description:
 * @date 2021-11-21 22:07
 */
public class FolderUtil {

    private static final int MAX_FILE_SIZE = 600;

    private static int CURRENT_FOLDER_SUFFIX_NUM = 1;

    private static String CURRENT_FOLDER_PATH = null;

    private static String INIT_FOLDER_PATH = null;

    public static File createFolder(String folderPath) {
        if (CURRENT_FOLDER_PATH != null) {
            folderPath = CURRENT_FOLDER_PATH;
        }
        File dir = new File(folderPath);
        if (!dir.exists()) {
            dir.mkdirs();
            CURRENT_FOLDER_PATH = folderPath;
            return dir;
        } else {
            int length = dir.list().length;
            if (MAX_FILE_SIZE < length) {
                int num = 0;
                System.out.println("文件" + folderPath + " size ：" + length + "大于" + MAX_FILE_SIZE);
                String[] fileNameArray = folderPath.split("_");
                if (fileNameArray.length - 1 != 0) {
                    num = Integer.parseInt(fileNameArray[fileNameArray.length - 1]);
                }
                String newFolderPath = fileNameArray[0] + "_" + (CURRENT_FOLDER_SUFFIX_NUM += num);
                CURRENT_FOLDER_PATH = newFolderPath;
                return createFolder(newFolderPath);
            } else {
              return dir;
            }
        }
    }

}
