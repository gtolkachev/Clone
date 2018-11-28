package com.company;

import Cache.AA;
import Cache.ConfigurableCache;

import java.io.*;
import java.util.HashMap;

import static java.lang.System.out;

public class Main{

    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchFieldException, IOException {
        AA aa = new AA(6574);

    out.println(ConfigurableCache.getObjectFields(aa).toString());
    Object bbbbb = ConfigurableCache.unpackObject(ConfigurableCache.getObjectFields(aa),aa.getClass());
    out.println(ConfigurableCache.getObjectFields(bbbbb).toString());

//        FileOutputStream fos = new FileOutputStream("hashmap.ser");
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(ConfigurableCache.getObjectFields(bbbbb).toString());
//        oos.close();
//        fos.close();
//
//        FileInputStream fis = new FileInputStream("hashmap.ser");
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        HashMap map = (HashMap) ois.readObject();
//        ois.close();
//        fis.close();
//    out.println(aa.hashCode());
//    out.println(bbbbb.hashCode());

    }
}
