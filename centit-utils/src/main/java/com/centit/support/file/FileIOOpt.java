package com.centit.support.file;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

@SuppressWarnings("unused")
public abstract class FileIOOpt {
    private FileIOOpt() {
        throw new IllegalAccessError("Utility class");
    }

    protected static final Logger logger = LoggerFactory.getLogger(FileIOOpt.class);

    public static int writeInputStreamToOutputStream(InputStream in,
            OutputStream out) throws IOException{
        int read = 0;
        int length=0;
        final byte[] bytes = new byte[1024 * 10];
        while ((read = in.read(bytes)) != -1){
            out.write(bytes, 0, read);
            length +=read;
        }
        return length;
    }

    public static int writeInputStreamToFile(InputStream in,
            File file) throws IOException{

        try(FileOutputStream out = new FileOutputStream(file, true)){
            return writeInputStreamToOutputStream(in,out);
        }
    }

    public static int writeInputStreamToFile(InputStream in,
            String filePath) throws IOException{
        return writeInputStreamToFile(in, new File(filePath));
    }

    public static void writeStringToOutputStream(String strData,OutputStream io) throws IOException{
        try(Writer writer = new OutputStreamWriter(io)){
            writer.write(strData);
        }
    }

    public static void writeStringToFile(String strData,File file) throws IOException{
        try(Writer writer = new FileWriter(file)){
            writer.write(strData);
        }
    }

    public static void writeStringToFile(String strData,String fileName) throws IOException{
        writeStringToFile(strData,new File(fileName));
    }

    public static String readStringFromRead(Reader reader) throws IOException{
        try(StringWriter writer = new StringWriter()){  
            char[] buf = new char[1024];
            int len;
            while ((len = reader.read(buf)) != -1) {
                writer.write(buf, 0, len);
            }
            return writer.toString();
        }
    }

    public static String readStringFromInputStream(InputStream is,String charsetName) throws IOException{
        return readStringFromRead(new InputStreamReader(is,charsetName));
    }

    public static String readStringFromInputStream(InputStream is) throws IOException{
        return readStringFromRead(new InputStreamReader(is));
    }

    public static String readStringFromFile(File file,String charsetName) throws IOException{
        return readStringFromRead(new InputStreamReader(new FileInputStream(file),charsetName));
    }

    public static String readStringFromFile(File file) throws IOException{
        return readStringFromRead(new InputStreamReader(new FileInputStream(file)));
    }

    public static String readStringFromFile(String fileName,String charsetName) throws IOException{
        return readStringFromFile(new File(fileName),charsetName);
    }

    public static String readStringFromFile(String fileName) throws IOException{
        return readStringFromFile(new File(fileName));
    }

    public static void writeObjectToFile(Object obj,String fileName) throws IOException{
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(obj);
        }
    }

    public static Object readObjectFromFile(String fileName)
            throws IOException, ClassNotFoundException{
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            return ois.readObject();
        }
    }

    public static void writeObjectAsJsonToFile(Object obj,String fileName) throws IOException{
        String sjson = JSON.toJSONString(obj);
        writeStringToFile(sjson,fileName);
    }

    public static <T> T readObjectAsJsonFromFile(String fileName, Class<T> clazz)
            throws IOException, ClassNotFoundException{
        String sjson = readStringFromFile(fileName);
        return JSON.parseObject(sjson,clazz);
    }

    /**
     * close the IO stream.
     * @param closeable closeable
     */
    public static void close(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
