 package mcp.mobius.waila;

 import mcp.mobius.waila.addons.ExternalModulesHandler;
 import mcp.mobius.waila.addons.vanillamc.HUDHandlerVanilla;
 import mcp.mobius.waila.handlers.HUDHandlerExternal;
 import mcp.mobius.waila.handlers.HUDHandlerWaila;
 import mcp.mobius.waila.handlers.SummaryProviderDefault;
 import mcp.mobius.waila.server.ProxyServer;
 import net.fabricmc.clienttools.lib.API;
 import net.fabricmc.clienttools.lib.ItemInfo;
 import net.minecraft.src.Item;

 public class ProxyClient
   extends ProxyServer
 {
   public void registerHandlers() {

     // GuiContainerManager.addTooltipHandler(new TooltipHandlerWaila());

     API.registerHighlightHandler(new HUDHandlerExternal(), ItemInfo.Layout.HEADER);
     API.registerHighlightHandler(new HUDHandlerExternal(), ItemInfo.Layout.BODY);
     API.registerHighlightHandler(new HUDHandlerWaila(), ItemInfo.Layout.FOOTER);
     API.registerHighlightHandler(new HUDHandlerWaila(), ItemInfo.Layout.HEADER);

     // KeyBindingRegistry.registerKeyBinding(new ConfigKeyHandler());

     ExternalModulesHandler.instance().registerShortDataProvider(new SummaryProviderDefault(), Item.class);

     registerMods();
   }


   public void registerMods() {
     HUDHandlerVanilla.register();
   }

 }


