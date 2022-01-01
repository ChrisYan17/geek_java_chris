package com.geek.chris.study.week1;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class GeekChrisFileClassLoader extends ClassLoader {

    private String filePath;

    public GeekChrisFileClassLoader(String filePath) {
        this.filePath = filePath;
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bs = doGetFileBytes(filePath);
        if (null != bs && bs.length > 0) {
            return defineClass(name, bs, 0, bs.length);
        }
        return null;
    }

    public byte[] doGetFileBytes(String filePath) {
        FileInputStream fi = null;
        ByteArrayOutputStream bo = null;
        try {
            fi = new FileInputStream(filePath);
            bo = new ByteArrayOutputStream();
            int i = -1;
            while ((i = fi.read()) >= 0) {
                i = 255 - i;
                bo.write(i);
            }
            return bo.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fi) {
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bo) {
                try {
                    bo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        GeekChrisFileClassLoader geekChrisClassLoader = new GeekChrisFileClassLoader(".\\src\\main\\java\\com\\geek\\chris\\study\\week1\\Hello.xlass");
        Class<?> cs = geekChrisClassLoader.findClass("Hello");
        Object object = cs.newInstance();
        cs.getMethod("hello").invoke(object);
    }
}
