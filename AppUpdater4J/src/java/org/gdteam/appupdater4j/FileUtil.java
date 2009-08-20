package org.gdteam.appupdater4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
