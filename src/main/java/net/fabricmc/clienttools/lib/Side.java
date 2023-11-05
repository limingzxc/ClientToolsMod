package net.fabricmc.clienttools.lib;

public enum Side {
    CLIENT, SERVER, BUKKIT;

    /**
     * @return If this is the server environment
     */
    public boolean isServer()
    {
        return !isClient();
    }

    /**
     * @return if this is the Client environment
     */
    public boolean isClient()
    {
        return this == CLIENT;
    }
}
