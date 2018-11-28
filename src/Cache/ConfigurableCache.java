package Cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigurableCache {
    private long levelOneCacheSize;
    private long levelTwoCacheSize;
    private String cacheStrategy;


    ConfigurableCache(int levelOneCacheSize, int levelTwoCacheSize, String cacheStrategy){
        this.levelOneCacheSize = levelOneCacheSize;
        this.levelTwoCacheSize = levelTwoCacheSize;
        this.cacheStrategy = cacheStrategy;
    }

    public Object getObject(int hashCode){
        return null;
    }

    public int putObject(Object object){
        int hashCode = object.hashCode();


        return hashCode;
    }


    private static boolean hasSuperClass (Class objClass) throws ClassNotFoundException {
        return(objClass.getSuperclass()!=null && !("java.lang.Object").equals(objClass.getName()));
    }

    public static Object unpackObject(HashMap<Field,?> map, Class objectClassName)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object returnObject = objectClassName.newInstance();
        for(Field field: map.keySet()){
            field.setAccessible(true);
            if(!Modifier.isFinal(field.getModifiers())){
                Class fieldType = field.getType();
                if(fieldType.isPrimitive()||"java.lang.String".equals(field.getType().getName())){                    //
                    setPrimitiveOrStringField(returnObject,field,map);
                }else{
                    Object fieldObject = field.getType().newInstance();
                    field.set(returnObject,unpackObject((HashMap<Field, ?>) map.get(field),fieldType));
                }
            }
        }
        return returnObject;
    }


    private static void getFieldValues(HashMap<Field, Object> objFieldsMap, Class clazz, Object object)
            throws IllegalAccessException, ClassNotFoundException {
        for(Field field: clazz.getDeclaredFields()){
            System.out.println(field.getName());
            field.setAccessible(true);
            if(!Modifier.isFinal(field.getModifiers())){
                Object fieldValue=field.get(object);
                if(fieldValue!=null){
                    if((field.getType().isPrimitive())||"java.lang.String".equals(field.getType().getName())){
                    } else{
                        fieldValue = getObjectFields(fieldValue);
                    }
                }
                objFieldsMap.put(field,fieldValue);
            }
        }
    }
    private static void setPrimitiveOrStringField(Object returnObject, Field field, HashMap<Field, ?> fieldMap)
            throws IllegalAccessException {
        String fieldTypeName = field.getType().toString();

        field.setAccessible(true);
        Object fieldValue = fieldMap.get(field);
        switch(fieldTypeName){
            case "boolean":  field.setBoolean(returnObject, (boolean)fieldValue);
                break;
            case "byte":  field.setByte(returnObject, (byte) fieldValue);
                break;
            case "char":  field.setChar(returnObject, (char)fieldValue);
                break;
            case "short":  field.setShort(returnObject, (short)fieldValue);
                break;
            case "int":  field.setInt(returnObject, (int)fieldValue);
                break;
            case "long":  field.setLong(returnObject, (long)fieldValue);
                break;
            case "float":  field.setFloat(returnObject, (float)fieldValue);
                break;
            case "double":  field.setDouble(returnObject, (double)fieldValue);
                break;
            case "class java.lang.String":  field.set(returnObject, (String)fieldValue);
                break;
        }
    }
    private static Object packPrimitiveOrStringField(Class<?> type, Object fieldValue) {
        //TODO
        return null;
    }

    public static HashMap<Field, Object> getObjectFields(Object object)
            throws ClassNotFoundException, IllegalAccessException {
        HashMap<Field, Object>  objFieldsMap = new HashMap<>();
        Class objClass = object.getClass();
        do{
            getFieldValues(objFieldsMap, objClass, object);
            for(Class clazz: objClass.getInterfaces()){
                getFieldValues(objFieldsMap, clazz, object);
            }
            objClass = objClass.getSuperclass();
        }while(hasSuperClass(objClass));
        return objFieldsMap;
    }

//    Field(Class<?> declaringClass,
//          String name,
//          Class<?> type,
//          int modifiers,
//          int slot,
//          String signature,
//          byte[] annotations)
//    {
//        this.clazz = declaringClass;
//        this.name = name;
//        this.type = type;
//        this.modifiers = modifiers;
//        this.slot = slot;
//        this.signature = signature;
//        this.annotations = annotations;
//    }

    private HashMap<String, Object> mapFieldInfo(Field field) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException {
        HashMap<String,Object> fieldInfoMap = new HashMap<>();
        String name = field.getName();
        fieldInfoMap.put("name", name);
        String ClassName = field.getDeclaringClass().getName();
        fieldInfoMap.put("ClassName", ClassName);

        Field typeField = field.getType().getField("type");
        typeField.setAccessible(true);
        Class typeClass = (Class) typeField.get(field);
        String type = typeClass.getName();
        fieldInfoMap.put("type", type);

        int modifiers = field.getModifiers();
        fieldInfoMap.put("modifiers", modifiers);
        Field slotField = field.getType().getField("slot");
        slotField.setAccessible(true);
        int slot = slotField.getInt(field);
        fieldInfoMap.put("slot", slot);
        Field signField = field.getType().getField("signature");
        signField.setAccessible(true);
        String signature = (String) slotField.get(field);
        fieldInfoMap.put("signature", signature);
        Field annotField = field.getType().getField("annotations");
        annotField.setAccessible(true);
        byte[] annotations = (byte[]) slotField.get(field);
        fieldInfoMap.put("annotations", annotations);

//        Object[] fld = new Object[]{Class.forName(ClassName),
//                name,
//                Class.forName(type),
//                modifiers,
//                slot,
//                signature,
//                annotations};
//
//
//        Constructor constructor = field.getClass().getDeclaredConstructors()[0];
//
//        Field renewField = (Field) constructor.newInstance(fld);

        return fieldInfoMap;
    }
}
