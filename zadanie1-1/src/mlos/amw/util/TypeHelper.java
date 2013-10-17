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
    
    public static String getWrapperName(String type) {
        return "java/lang/" + WRAPPERS.get(type);
    }
    
    
    public static String getMethodType(String desc) {
        int from = desc.indexOf(')');
        return desc.substring(from + 1);
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
