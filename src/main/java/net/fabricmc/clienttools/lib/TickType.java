package net.fabricmc.clienttools.lib;

import java.util.EnumSet;

public enum TickType {
    /**
     * Fired during the world evaluation loop
     * server and client side
     *
     * arg 0 : The world that is ticking
     */
    WORLD,
    /**
     * client side
     * Fired during the render processing phase
     * arg 0 : float "partial render time"
     */
    RENDER,
    /**
     * server side
     * Fired once as the world loads from disk
     */
    WORLDLOAD,
    /**
     * client side only
     * Fired once per client tick loop.
     */
    CLIENT,
    /**
     * client and server side.
     * Fired whenever the players update loop runs.
     * arg 0 : the player
     * arg 1 : the world the player is in
     */
    PLAYER,
    /**
     * server side only.
     * This is the server game tick.
     * Fired once per tick loop on the server.
     */
    SERVER;

    /**
     * Partner ticks that are also cancelled by returning false from onTickInGame
     */
    public EnumSet<TickType> partnerTicks()
    {
        if (this==CLIENT) return EnumSet.of(RENDER);
        if (this==RENDER) return EnumSet.of(CLIENT);
        return EnumSet.noneOf(TickType.class);
    }
}