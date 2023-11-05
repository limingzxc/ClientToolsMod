package net.fabricmc.clienttools.mixin;

import net.fabricmc.clienttools.api.GuiButtonApi;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(GuiScreen.class)
public class GuiScreenMixin implements GuiButtonApi {
    @Shadow protected List<GuiButton> buttonList;

    @Override
    public List<GuiButton> getButtonList() {
        return buttonList;
    }

}
