package net.fabricmc.clienttools.api;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;

public interface AutoClick {

    default void setListeningForAutoHarvestMode() {}

    default void setAutoHarvestMode(int x, int y, int z) {}

    default void clearAutoHarvestMode() { }

    default boolean autoHarvestModeHasExpired() { return false; }

    default boolean matchesAutoHarvestBlock(int x, int y, int z) { return false; }


    default void setLastAutoHarvestMs(long last_auto_harvest_ms) {}


    default boolean isInBed(EntityPlayer player) { return false; }


    default Block getAutoHarvestBlock() { return null; }

    default boolean getListeningForAutoHarvestModeClick() { return false; }


    default boolean getCancelAutoHarvestOnNextClick() { return false; }

    default void setCancelAutoHarvestOnNextClick(boolean cancel_auto_harvest_on_next_click) {}
}
