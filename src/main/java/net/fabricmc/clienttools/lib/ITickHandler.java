package net.fabricmc.clienttools.lib;

import java.util.EnumSet;

public interface ITickHandler
{

    /**
     * Called at the "start" phase of a tick
     *
     * Multiple ticks may fire simultaneously- you will only be called once with all the firing ticks
     *
     */
    public void tickStart(EnumSet<TickType> type, Object... tickData);

    /**
     * Called at the "end" phase of a tick
     *
     * Multiple ticks may fire simultaneously- you will only be called once with all the firing ticks
     *
     */
    public void tickEnd(EnumSet<TickType> type, Object... tickData);

    /**
     * Returns the list of ticks this tick handler is interested in receiving at the minute
     */
    public EnumSet<TickType> ticks();

    /**
     * A profiling label for this tick handler
     */
    public String getLabel();
}

