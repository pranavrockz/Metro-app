package com.example.quizapp;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class FileUtil {
    public static void copyAssetFileToInternalStorage(Context context, int assetFileName, String destinationFileName) throws IOException {
        InputStream in = context.getResources().openRawResource(assetFileName);
        File outFile = new File(context.getFilesDir(), destinationFileName);
        OutputStream out = new FileOutputStream(outFile);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }
}
