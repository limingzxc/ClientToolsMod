 package mcp.mobius.waila.addons;

 import btw.community.clienttools.ClientToolsAddon;
 import mcp.mobius.waila.api.IWailaSummaryProvider;
 import net.minecraft.src.Block;
 import au.com.bytecode.opencsv.CSVReader;
 import mcp.mobius.waila.api.IWailaDataProvider;
 import mcp.mobius.waila.api.IWailaRegistrar;

 import java.io.*;
 import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.util.ArrayList;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Set;
 import java.util.logging.Level;


 public class ExternalModulesHandler
   implements IWailaRegistrar
 {
   private static ExternalModulesHandler instance = null;
   public LinkedHashMap<Integer, ArrayList<IWailaDataProvider>> headProviders = new LinkedHashMap<>();
   public LinkedHashMap<Integer, ArrayList<IWailaDataProvider>> bodyProviders = new LinkedHashMap<>();
   public LinkedHashMap<Integer, ArrayList<IWailaDataProvider>> tailProviders = new LinkedHashMap<>();
   public LinkedHashMap<Integer, ArrayList<IWailaDataProvider>> stackProviders = new LinkedHashMap<>();

   public LinkedHashMap<Class<?>, ArrayList<IWailaDataProvider>> headBlockProviders = new LinkedHashMap<>();
   public LinkedHashMap<Class<?>, ArrayList<IWailaDataProvider>> bodyBlockProviders = new LinkedHashMap<>();
   public LinkedHashMap<Class<?>, ArrayList<IWailaDataProvider>> tailBlockProviders = new LinkedHashMap<>();
   public LinkedHashMap<Object, Object> stackBlockProviders = new LinkedHashMap<>();

   public LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> wikiDescriptions = new LinkedHashMap<>();

   public LinkedHashMap<Class<?>, ArrayList<IWailaSummaryProvider>> summaryProviders = new LinkedHashMap<>();

   private ExternalModulesHandler() {
     instance = this;
   }

   public static ExternalModulesHandler instance() {
     if (instance == null)
       instance = new ExternalModulesHandler();
     return instance;
   }


   public void addConfig(String modname, String key, String configname) {
     ConfigHandler.instance().addConfig(modname, key, configname);
   }


   public void addConfigRemote(String modname, String key, String configname) {
     ConfigHandler.instance().addConfigServer(modname, key, configname);
   }


   public void registerHeadProvider(IWailaDataProvider dataProvider, int blockID) {
     if (!this.headProviders.containsKey(blockID))
       this.headProviders.put(blockID, new ArrayList<>());
     this.headProviders.get(blockID).add(dataProvider);
   }


   public void registerHeadProvider(IWailaDataProvider dataProvider, Class<?> block) {
     if (!this.headBlockProviders.containsKey(block))
       this.headBlockProviders.put(block, new ArrayList<>());
     this.headBlockProviders.get(block).add(dataProvider);
   }


   public void registerBodyProvider(IWailaDataProvider dataProvider, int blockID) {
     if (!this.bodyProviders.containsKey(blockID))
       this.bodyProviders.put(blockID, new ArrayList<>());
     this.bodyProviders.get(blockID).add(dataProvider);
   }


   public void registerBodyProvider(IWailaDataProvider dataProvider, Class<?> block) {
     if (!this.bodyBlockProviders.containsKey(block))
       this.bodyBlockProviders.put(block, new ArrayList<>());
     this.bodyBlockProviders.get(block).add(dataProvider);
   }

   public void registerTailProvider(IWailaDataProvider dataProvider, int blockID) {
     if (!this.tailProviders.containsKey(blockID))
       this.tailProviders.put(blockID, new ArrayList<>());
     this.tailProviders.get(blockID).add(dataProvider);
   }

   public void registerTailProvider(IWailaDataProvider dataProvider, Class<?> block) {
     if (!this.tailBlockProviders.containsKey(block))
       this.tailBlockProviders.put(block, new ArrayList<>());
     this.tailBlockProviders.get(block).add(dataProvider);
   }


   public void registerStackProvider(IWailaDataProvider dataProvider, int blockID) {
     if (!this.stackProviders.containsKey(blockID))
       this.stackProviders.put(blockID, new ArrayList<>());
     this.stackProviders.get(blockID).add(dataProvider);
   }


   public void registerStackProvider(IWailaDataProvider dataProvider, Class<?> block) {
     for (int i = 0; i < Block.blocksList.length; i++) {
       if (block.isInstance(Block.blocksList[i])) {
         registerStackProvider(dataProvider, i);
       }
     }
   }

   public void registerShortDataProvider(IWailaSummaryProvider dataProvider, Class<?> item) {
     if (!this.summaryProviders.containsKey(item))
       this.summaryProviders.put(item, new ArrayList<>());
     this.summaryProviders.get(item).add(dataProvider);
   }



   public ArrayList<IWailaDataProvider> getHeadProviders(int blockID) {
     return this.headProviders.get(blockID);
   }

   public ArrayList<IWailaDataProvider> getBodyProviders(int blockID) {
     return this.bodyProviders.get(blockID);
   }

   public ArrayList<IWailaDataProvider> getTailProviders(int blockID) {
     return this.tailProviders.get(blockID);
   }

   public ArrayList<IWailaDataProvider> getHeadProviders(Object block) {
     ArrayList<IWailaDataProvider> returnList = new ArrayList<>();
     for (Class<?> clazz : this.headBlockProviders.keySet()) {
       if (clazz.isInstance(block))
         returnList.addAll(this.headBlockProviders.get(clazz));
     }
     return returnList;
   }

   public ArrayList<IWailaDataProvider> getBodyProviders(Object block) {
     ArrayList<IWailaDataProvider> returnList = new ArrayList<>();
     for (Class<?> clazz : this.bodyBlockProviders.keySet()) {
       if (clazz.isInstance(block))
         returnList.addAll(this.bodyBlockProviders.get(clazz));
     }
     return returnList;
   }

   public ArrayList<IWailaDataProvider> getTailProviders(Object block) {
     ArrayList<IWailaDataProvider> returnList = new ArrayList<>();
     for (Class<?> clazz : this.bodyBlockProviders.keySet()) {
       if (clazz.isInstance(block))
         returnList.addAll(this.bodyBlockProviders.get(clazz));
     }
     return returnList;
   }

   public ArrayList<IWailaDataProvider> getStackProviders(int blockID) {
     return this.stackProviders.get(blockID);
   }

   public ArrayList<IWailaSummaryProvider> getSummaryProvider(Object item) {
     ArrayList<IWailaSummaryProvider> returnList = new ArrayList<>();
     for (Class<?> clazz : this.summaryProviders.keySet()) {
       if (clazz.isInstance(item))
         returnList.addAll(this.summaryProviders.get(clazz));
     }
     return returnList;
   }

   public boolean hasHeadProviders(int blockID) {
     return this.headProviders.containsKey(blockID);
   }

   public boolean hasBodyProviders(int blockID) {
     return this.bodyProviders.containsKey(blockID);
   }

   public boolean hasTailProviders(int blockID) {
     return this.tailProviders.containsKey(blockID);
   }

   public boolean hasStackProviders(int blockID) {
     return this.stackProviders.containsKey(blockID);
   }

   public boolean hasHeadProviders(Object block) {
     for (Class<?> clazz : this.headBlockProviders.keySet()) {
       if (clazz.isInstance(block))
         return true;
     }  return false;
   }

   public boolean hasBodyProviders(Object block) {
     for (Class<?> clazz : this.bodyBlockProviders.keySet()) {
       if (clazz.isInstance(block))
         return true;
     }  return false;
   }

   public boolean hasTailProviders(Object block) {
     for (Class<?> clazz : this.tailBlockProviders.keySet()) {
       if (clazz.isInstance(block))
         return true;
     }  return false;
   }

   public boolean hasSummaryProvider(Class<?> item) {
     return this.summaryProviders.containsKey(item);
   }


   public void registerDocTextFile(String filename) {
     List<String[]> docData = null;
     int nentries = 0;


     try {
       docData = readFileAsString(filename);
     } catch (IOException e) {
       ClientToolsAddon.ModLogger("Error while accessing file %s : %s");

       return;
     }
     for (String[] ss : docData) {
       String modid = ss[0];
       String name = ss[1];
       String meta = ss[2];
       String desc = ss[5].replace('$', '\n');
       if (!desc.trim().equals("")) {
         if (!this.wikiDescriptions.containsKey(modid))
           this.wikiDescriptions.put(modid, new LinkedHashMap<>());
         if (!(this.wikiDescriptions.get(modid)).containsKey(name)) {
           this.wikiDescriptions.get(modid).put(name, new LinkedHashMap<>());
         }
         this.wikiDescriptions.get(modid).get(name).put(meta, desc);
         System.out.printf("Registered %s %s %s\n", modid, name, meta);
         nentries++;
       }
     }
     ClientToolsAddon.ModLogger("Registered %s entries from %s");
   }

   public boolean hasDocTextModID(String modid) {
     return this.wikiDescriptions.containsKey(modid);
   }

   public boolean hasDocTextItem(String modid, String item) {
     if (hasDocTextModID(modid))
       return this.wikiDescriptions.get(modid).containsKey(item);
     return false;
   }

   public boolean hasDocTextMeta(String modid, String item, String meta) {
     if (hasDocTextItem(modid, item))
       return this.wikiDescriptions.get(modid).get(item).containsKey(meta);
     return false;
   }

   public LinkedHashMap<String, String> getDocText(String modid, String name) {
     return this.wikiDescriptions.get(modid).get(name);
   }

   public String getDocText(String modid, String name, String meta) {
     return this.wikiDescriptions.get(modid).get(name).get(meta);
   }

   public boolean hasDocTextSpecificMeta(String modid, String name, String meta) {
     for (String s : getDocText(modid, name).keySet()) {
       if (s.equals(meta))
         return true;
     }  return false;
   }

   public String getDoxTextWildcardMatch(String modid, String name) {
     Set<String> keys = this.wikiDescriptions.get(modid).keySet();
     for (String s : keys) {
       String regexed = s;
       regexed = regexed.replace(".", "\\.");
       regexed = regexed.replace("*", ".*");
       if (name.matches(s))
         return s;
     }
     return null;
   }

   private List<String[]> readFileAsString(String filePath) throws IOException {
     InputStream in = Files.newInputStream(Paths.get(filePath));
     BufferedReader input = new BufferedReader(new InputStreamReader(in));
     CSVReader reader = new CSVReader(input);

     List<String[]> myEntries = reader.readAll();
     reader.close();

     return myEntries;
   }
 }


