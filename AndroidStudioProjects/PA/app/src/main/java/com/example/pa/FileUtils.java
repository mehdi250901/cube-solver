package com.example.pa;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static File createZipFile(List<String> filePaths, String zipFilePath) throws IOException {
        File zipFile = new File(zipFilePath);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            byte[] buffer = new byte[1024];
            for (String filePath : filePaths) {
                File file = new File(filePath);
                Log.d(TAG, "Adding file to ZIP: " + filePath); // Log each file added
                try (FileInputStream fis = new FileInputStream(file);
                     BufferedInputStream bis = new BufferedInputStream(fis)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        zos.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
        Log.d(TAG, "ZIP file created at: " + zipFilePath); // Log ZIP file path
        return zipFile;
    }
}
