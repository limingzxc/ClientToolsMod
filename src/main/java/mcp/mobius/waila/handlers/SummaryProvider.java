 package mcp.mobius.waila.handlers;

 import java.util.LinkedHashMap;
 import mcp.mobius.waila.addons.ExternalModulesHandler;
 import mcp.mobius.waila.api.IWailaConfigHandler;
 import mcp.mobius.waila.api.IWailaSummaryProvider;
 import net.minecraft.src.ItemStack;


 public class SummaryProvider
 {
   private static SummaryProvider instance = null;

   private SummaryProvider() {
     instance = this;
   }

   public static SummaryProvider instance() {
     if (instance == null)
       instance = new SummaryProvider();
     return instance;
   }

   public LinkedHashMap<String, String> getSummary(ItemStack stack, IWailaConfigHandler config) {
     LinkedHashMap<String, String> currentSummary = new LinkedHashMap<>();

     for (IWailaSummaryProvider provider : ExternalModulesHandler.instance().getSummaryProvider(stack.getItem())) {
       currentSummary = provider.getSummary(stack, currentSummary, config);
     }
     return currentSummary;
   }
 }


