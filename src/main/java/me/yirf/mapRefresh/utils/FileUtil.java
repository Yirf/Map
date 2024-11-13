package me.yirf.mapRefresh.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtil {

    public static void copy(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }

            String[] files = source.list();
            if (files == null) return;
            for (String file : files) {
                File newSource = new File(source, file);
                File newDestination = new File(destination, file);
                copy(newSource, newDestination);
            }
        } else {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(destination);

            byte[] buf = new byte[1024];

            int length;

            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }

            in.close();
            out.close();
        }
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return;
            for (File child : files) {
                delete(child);
            }
        }

        file.delete();
    }

    public static List<String> worldStrings(File file) {
        if (file == null) {
            return Collections.emptyList();  // Return an empty list if the file is null
        }

        List<String> list = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return Collections.emptyList();  // Return empty if directory has no files or can't be read
            }
            for (File child : files) {
                list.add(child.getName());
            }
        } else {
            list.add(file.getName());  // Add the file name if it's not a directory
        }
        return list;
    }
}
