package twoinonecard.com.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import twoinonecard.com.R;

/**
 * Created by Administrator on 2018/3/29.
 */

public class CopyData {


    /**
     * 将raw下的文件复制到sdCard
     */
    public static boolean setBmpDecodeEnv(Context context) {

        String path=context.getFilesDir().getAbsolutePath();
        String filename = "base.dat";
        try{
            String databaseFilename = path + "/" + filename;
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdir();
            if (!(new File(databaseFilename)).exists()){
                InputStream is = context.getResources().openRawResource(R.raw.base);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        String filename1 = "license.lic";
        try{
            String databaseFilename = path + "/" + filename1;
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdir();
            if (!(new File(databaseFilename)).exists()){
                InputStream is = context.getResources().openRawResource(R.raw.license);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public static byte[] StringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 数组转成十六进制字符串
     */
    public static String toHexString1(byte[] b){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i){
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }
    public static String toHexString1(byte b){
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1){
            return "0" + s;
        }else{
            return s;
        }
    }
}
