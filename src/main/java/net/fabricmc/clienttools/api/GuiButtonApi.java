package net.fabricmc.clienttools.api;

import net.minecraft.src.GuiButton;

import java.util.List;

public interface GuiButtonApi {

    default List<GuiButton> getButtonList() {
        return null;
    }
}
