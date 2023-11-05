package mcp.mobius.waila.tools;

import java.lang.reflect.Field;

public class Reflect
 {
   public static Object getFieldValue(String className, String fieldname, Object obj) {
     try {
       Class<?> class_ = Class.forName(className);
       Field field_ = class_.getField(fieldname);
       field_.setAccessible(true);
               return field_.get(obj);
     }
     catch (Exception e) {
       e.printStackTrace();

       return null;
     }
   }
}
