 package mcp.mobius.waila.handlers;

 import btw.community.clienttools.ClientToolsAddon;
 import net.fabricmc.clienttools.lib.ItemInfo;
 import net.minecraft.src.World;
 import net.minecraft.src.MovingObjectPosition;
 import java.util.List;
 import mcp.mobius.waila.addons.ConfigHandler;
 import net.fabricmc.clienttools.lib.IHighlightHandler;
 import net.minecraft.src.EntityPlayer;
 import net.minecraft.src.ItemStack;

 public class HUDHandlerWaila implements IHighlightHandler
 {
   public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop) {
     return null;
   }



   public List<String> handleTextData(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop, List<String> currenttip, ItemInfo.Layout layout) {
     if (layout == ItemInfo.Layout.FOOTER) {
       String modName = ClientToolsAddon.instance.getModName(itemStack);
       if (modName != null && !modName.equals(""))
         currenttip.add("§9§o" + modName);
     } else if (layout == ItemInfo.Layout.HEADER && ConfigHandler.instance().getConfig("waila.showmetadata", false)) {
//       if (currenttip.size() == 0) {
//         currenttip.add("< Unnamed >");
//       } else {
//         String name = currenttip.get(0);
//         currenttip.set(0, name + String.format(" %s:%s", world.getBlockId(mop.blockX, mop.blockY, mop.blockZ), world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ)));
//       }
         if(mop != null) {
             int blockId = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
             if(blockId == 149 || blockId == 150) blockId = 404;
             else if(blockId ==93 || blockId == 94) blockId = 356;
             currenttip.add(String.format("%s:%s", blockId, world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ)));
         }

     }
     return currenttip;
   }
 }


