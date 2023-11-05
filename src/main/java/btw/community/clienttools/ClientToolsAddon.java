package btw.community.clienttools;

import btw.AddonHandler;
import btw.BTWAddon;
import mcp.mobius.waila.ProxyClient;
import net.minecraft.src.ItemStack;

public class ClientToolsAddon extends BTWAddon {

    public static ClientToolsAddon instance;

    public boolean serverPresent = false;

    public static ProxyClient proxy;

    private ClientToolsAddon() {
        super("Client Tools", "1.0.0", "Ex");
    }

    @Override
    public void initialize() {
        load();
        ModLogger(this.getName() + " Version " + this.getVersionString()
                + " Initializing...");
        
    }

    public static void ModLogger(String s) {
        AddonHandler.logMessage(s);
    }

    public String getModName(ItemStack itemStack) {
        if(itemStack != null)
            return itemStack.getItem().getItemDisplayName(itemStack);
        else return "BTW";
    }

    private void load() {
        instance = new ClientToolsAddon();
        proxy = new ProxyClient();
        proxy.registerHandlers();
    }

}
