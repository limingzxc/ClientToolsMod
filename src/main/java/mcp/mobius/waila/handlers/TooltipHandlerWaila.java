 package mcp.mobius.waila.handlers;

 import net.minecraft.src.GuiContainer;
 import java.util.List;
 import btw.community.clienttools.ClientToolsAddon;
 import net.minecraft.src.ItemStack;

 public class TooltipHandlerWaila {
   private final ClientToolsAddon waila = ClientToolsAddon.instance;


   public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
     return currenttip;
   }


   public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
     String canonicalName = this.waila.getModName(itemstack);
     if (canonicalName != null && !canonicalName.equals(""))
       currenttip.add("§9§o" + canonicalName);
     return currenttip;
   }
 }


