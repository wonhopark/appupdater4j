package org.gdteam.appupdater4j;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {

    public static String getMD5(InputStream is) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void copy(InputStream in, OutputStream out, int blocksize) throws IOException {
        byte[] b = new byte[blocksize];
        while (true) {
            int l = in.read(b);
            if (l <= 0) {
                break;
            }
            out.write(b, 0, l);
        }
    }
    
    public static final void unzipFile(File file, String dir) throws Exception{
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    // Do nothing. Directory will be created after
                } else {
                    File fileToExtract = new File(dir + entry.getName());
                    File parent = fileToExtract.getParentFile();

                    if (parent != null) {
                        parent.mkdirs();
                    }
                    
                    InputStream is = null;
                    OutputStream out = null;
                    
                    try {
                        is = zipFile.getInputStream(entry);
                        out = new BufferedOutputStream(new FileOutputStream(fileToExtract));
                        
                        byte[] buffer = new byte[1024];
                        int len;

                        while ((len = is.read(buffer)) >= 0) {
                            out.write(buffer, 0, len);
                        }
                    } finally {
                        if (is != null){
                            is.close();
                        }
                        if (out != null){
                            out.close();
                        }
                    }

                }
            }
        
        } finally {
            if (zipFile != null){
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 
        }
    }

}
