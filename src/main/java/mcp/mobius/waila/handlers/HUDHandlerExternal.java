 package mcp.mobius.waila.handlers;

 import mcp.mobius.waila.WailaExceptionHandler;
 import mcp.mobius.waila.addons.ConfigHandler;
 import mcp.mobius.waila.addons.ExternalModulesHandler;
 import mcp.mobius.waila.api.IWailaBlock;
 import mcp.mobius.waila.api.IWailaDataProvider;
 import net.fabricmc.clienttools.lib.IHighlightHandler;
 import net.fabricmc.clienttools.lib.ItemInfo;
 import net.minecraft.src.*;

 import java.util.List;

 public class HUDHandlerExternal implements IHighlightHandler {

   public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop) {
     DataAccessor accessor = DataAccessor.instance;
     accessor.set(world, player, mop);
     Block block = accessor.getBlock();
     int blockID = accessor.getBlockID();

     if (block instanceof IWailaBlock) {
       try {
         return ((IWailaBlock)block).getWailaStack(accessor, ConfigHandler.instance());
       } catch (Throwable e) {
         WailaExceptionHandler.handleErr(e, block.getClass().toString(), null);
       }
     }

     if (ExternalModulesHandler.instance().hasStackProviders(blockID)) {
       for (IWailaDataProvider dataProvider : ExternalModulesHandler.instance().getStackProviders(blockID)) {
         try {
           ItemStack retval = dataProvider.getWailaStack(accessor, ConfigHandler.instance());
           if (retval != null)
             return retval;
         } catch (Throwable e) {
           WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), null);
         }
       }
     }
     return null;
   }


   public List<String> handleTextData(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop, List<String> currenttip, ItemInfo.Layout layout) {
     DataAccessor accessor = DataAccessor.instance;
     accessor.set(world, player, mop);
     Block block = accessor.getBlock();
     int blockID = accessor.getBlockID();

//     if (accessor.getTileEntity() != null && ClientToolsAddon.instance.serverPresent && System.currentTimeMillis() - accessor.timeLastUpdate >= 250L) {
//       accessor.timeLastUpdate = System.currentTimeMillis();
//       PacketDispatcher.sendPacketToServer(Packet0x01TERequest.create(world, mop));
//     }
//
//     if (block instanceof IWailaBlock) {
//       TileEntity entity = world.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
//       if (layout == ItemInfo.Layout.HEADER)
//         try {
//           return ((IWailaBlock)block).getWailaHead(itemStack, currenttip, accessor, ConfigHandler.instance());
//         } catch (Throwable e) {
//           return WailaExceptionHandler.handleErr(e, block.getClass().toString(), currenttip);
//         }
//       if (layout == ItemInfo.Layout.BODY) {
//         try {
//           return ((IWailaBlock)block).getWailaBody(itemStack, currenttip, accessor, ConfigHandler.instance());
//         } catch (Throwable e) {
//           return WailaExceptionHandler.handleErr(e, block.getClass().toString(), currenttip);
//         }
//       }
//     }

     if (layout == ItemInfo.Layout.HEADER && ExternalModulesHandler.instance().hasHeadProviders(blockID)) {
       for (IWailaDataProvider dataProvider : ExternalModulesHandler.instance().getHeadProviders(blockID)) {
         try {
           currenttip = dataProvider.getWailaHead(itemStack, currenttip, accessor, ConfigHandler.instance());
         } catch (Throwable e) {
           WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
         }
       }
     }
     if (layout == ItemInfo.Layout.BODY && ExternalModulesHandler.instance().hasBodyProviders(blockID)) {
       for (IWailaDataProvider dataProvider : ExternalModulesHandler.instance().getBodyProviders(blockID)) {
         try {
           currenttip = dataProvider.getWailaBody(itemStack, currenttip, accessor, ConfigHandler.instance());
         } catch (Throwable e) {
           WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
         }
       }
     }
     if (layout == ItemInfo.Layout.HEADER && ExternalModulesHandler.instance().hasHeadProviders(block)) {
       for (IWailaDataProvider dataProvider : ExternalModulesHandler.instance().getHeadProviders(block)) {
         try {
           currenttip = dataProvider.getWailaHead(itemStack, currenttip, accessor, ConfigHandler.instance());
         } catch (Throwable e) {
           WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
         }
       }
     }
     if (layout == ItemInfo.Layout.BODY && ExternalModulesHandler.instance().hasBodyProviders(block)) {
       for (IWailaDataProvider dataProvider : ExternalModulesHandler.instance().getBodyProviders(block)) {
         try {
           currenttip = dataProvider.getWailaBody(itemStack, currenttip, accessor, ConfigHandler.instance());
         } catch (Throwable e) {
           WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
         }
       }
     }
     if (layout == ItemInfo.Layout.HEADER && ExternalModulesHandler.instance().hasHeadProviders(accessor.getTileEntity())) {
       for (IWailaDataProvider dataProvider : ExternalModulesHandler.instance().getHeadProviders(accessor.getTileEntity())) {
         try {
           currenttip = dataProvider.getWailaHead(itemStack, currenttip, accessor, ConfigHandler.instance());
         } catch (Throwable e) {
           WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
         }
       }
     }
     if (layout == ItemInfo.Layout.BODY && ExternalModulesHandler.instance().hasBodyProviders(accessor.getTileEntity())) {
       for (IWailaDataProvider dataProvider : ExternalModulesHandler.instance().getBodyProviders(accessor.getTileEntity())) {
         try {
           currenttip = dataProvider.getWailaBody(itemStack, currenttip, accessor, ConfigHandler.instance());
         } catch (Throwable e) {
           WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
         }
       }
     }
     return currenttip;
   }
 }


