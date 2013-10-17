package mlos.amw.util;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public final class TypeHelper {

    private TypeHelper() {
        // non-instantiable
    }
    
    public final static Map<String, String> WRAPPERS = ImmutableMap.
            <String, String>builder()
            .put("Z", "Boolean")
            .put("C", "Character")
            .put("B", "Byte")
            .put("S", "Short")
            .put("I", "Integer")
            .put("J", "Long")
            .put("F", "Float")
            .put("D", "Double")
            .build();
    
    
    public final static Map<String, String> PRIMITIVES = ImmutableMap.
            <String, String>builder()
            .put("Z", "boolean")
            .put("C", "char")
            .put("B", "byte")
            .put("S", "short")
            .put("I", "int")
            .put("J", "long")
            .put("F", "float")
            .put("D", "double")
            .build();
    
    public static String getWrapperName(String desc) {
        return "java/lang/" + WRAPPERS.get(desc);
    }
    
    public static String getPrimitiveName(String desc) {
        return PRIMITIVES.get(desc);
    }
    
    public static String getClassName(String desc) {
        int n = desc.length();
        return desc.substring(1, n - 1).replace('/', '.');
    }
    
    public static String getMethodType(String desc) {
        int from = desc.indexOf(')');
        return desc.substring(from + 1);
    }
    
    public static String prettyPrint(String desc) {
        return isPrimitive(desc) ? getPrimitiveName(desc) : getClassName(desc);
    }
    
    
    public static boolean isPrimitive(String type) {
        return WRAPPERS.containsKey(type);
    }
    
    public static int category(String type) {
        if (type.equals("D") || type.equals("J")) {
            return 2;
        } else {
            return 1;
        }
    }
    

}
