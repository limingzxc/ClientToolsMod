 package mcp.mobius.waila;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.logging.Level;

 public class WailaExceptionHandler
 {
   private static final ArrayList<String> errs = new ArrayList<>();

   public static List<String> handleErr(Throwable e, String className, List<String> currenttip) {
     if (!errs.contains(className)) {
       errs.add(className);
       // mod_Waila.log.log(Level.WARNING, String.format("Catched unhandled exception : [%s] %s", new Object[] { className, e }));
     }
     if (currenttip != null) {
       currenttip.add("<ERROR>");
     }
     return currenttip;
   }
 }


