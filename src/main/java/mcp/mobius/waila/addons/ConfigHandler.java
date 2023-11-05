 package mcp.mobius.waila.addons;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.LinkedHashMap;
 import java.util.Set;

 import btw.community.clienttools.ClientToolsAddon;
 import mcp.mobius.waila.api.IWailaConfigHandler;
 // import net.minecraftforge.common.Property;


 public class ConfigHandler
   implements IWailaConfigHandler
 {
   private static ConfigHandler instance = null;
   private final LinkedHashMap<String, ConfigModule> modules = new LinkedHashMap<>();
   private final ArrayList<String> serverconfigs = new ArrayList<>();

   private ConfigHandler() {
     instance = this;
   }

   public static ConfigHandler instance() {
     if (instance == null)
       instance = new ConfigHandler();
     return instance;
   }

   public void addModule(String modName, HashMap<String, String> options) {
     addModule(modName, new ConfigModule(modName, options));
   }

   public void addModule(String modName, ConfigModule options) {
     this.modules.put(modName, options);
   }


   public Set<String> getModuleNames() {
     return this.modules.keySet();
   }


   public HashMap<String, String> getConfigKeys(String modName) {
     if (this.modules.containsKey(modName)) {
       return this.modules.get(modName).options;
     }
     return null;
   }

   public void addConfig(String modName, String key, String name) {
     if (!this.modules.containsKey(modName)) {
       this.modules.put(modName, new ConfigModule(modName));
     }
     this.modules.get(modName).addOption(key, name);
   }

   public void addConfigServer(String modName, String key, String name) {
     if (!this.modules.containsKey(modName)) {
       this.modules.put(modName, new ConfigModule(modName));
     }
     this.modules.get(modName).addOption(key, name);
     this.serverconfigs.add(key);
   }


   public boolean getConfig(String key, boolean defvalue) {
     // mod_Waila.instance.config.load();

     if (this.serverconfigs.contains(key) && !ClientToolsAddon.instance.serverPresent) {
       return false;
     }
     // Property prop = ClientToolsAddon.instance.config.get("general", key, defvalue);
     // return prop.getBoolean(defvalue);
     return true;
   }

   public boolean isServerRequired(String key) {
     return this.serverconfigs.contains(key);
   }


   public boolean getConfig(String key) {
     return getConfig(key, true);
   }
 }

