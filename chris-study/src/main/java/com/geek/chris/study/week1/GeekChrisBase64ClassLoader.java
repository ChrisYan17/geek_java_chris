package com.geek.chris.study.week1;

import sun.misc.BASE64Decoder;

public class GeekChrisBase64ClassLoader extends ClassLoader {

    private String signMsg;

    public GeekChrisBase64ClassLoader(String signMsg) {
        this.signMsg = signMsg;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] bs = (new BASE64Decoder()).decodeBuffer(signMsg);
            if (null != bs && bs.length > 0) {
                return defineClass(name, bs, 0, bs.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) throws Exception {
        GeekChrisBase64ClassLoader geekChrisClassLoader = new GeekChrisBase64ClassLoader("yv66vgAAADQAHAoABgAOCQAPABAIABEKABIAEwcAFAcAFQEABjxpbml0PgEAAygpVgEABENvZGUB\n" +
                "AA9MaW5lTnVtYmVyVGFibGUBAAVoZWxsbwEAClNvdXJjZUZpbGUBAApIZWxsby5qYXZhDAAHAAgH\n" +
                "ABYMABcAGAEAE0hlbGxvLCBjbGFzc0xvYWRlciEHABkMABoAGwEABUhlbGxvAQAQamF2YS9sYW5n\n" +
                "L09iamVjdAEAEGphdmEvbGFuZy9TeXN0ZW0BAANvdXQBABVMamF2YS9pby9QcmludFN0cmVhbTsB\n" +
                "ABNqYXZhL2lvL1ByaW50U3RyZWFtAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgAh\n" +
                "AAUABgAAAAAAAgABAAcACAABAAkAAAAdAAEAAQAAAAUqtwABsQAAAAEACgAAAAYAAQAAAAEAAQAL\n" +
                "AAgAAQAJAAAAJQACAAEAAAAJsgACEgO2AASxAAAAAQAKAAAACgACAAAABAAIAAUAAQAMAAAAAgAN");
        Class<?> cs = geekChrisClassLoader.findClass("Hello");
        Object object = cs.newInstance();
        cs.getMethod("hello").invoke(object);
    }
}
