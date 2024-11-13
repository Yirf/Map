package me.yirf.mapRefresh.utils;

import java.io.*;

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
}
