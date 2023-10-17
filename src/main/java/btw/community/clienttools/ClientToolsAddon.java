package btw.community.clienttools;

import btw.AddonHandler;
import btw.BTWAddon;

public class ClientToolsAddon extends BTWAddon {

    private ClientToolsAddon() {
        super("Client Tools", "1.0.0", "Ex");
    }

    @Override
    public void initialize() {

        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString()
                + " Initializing...");

    }

}
