package net.fabricmc.clienttools.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiIngame;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {
    @Final
    @Shadow
    private Minecraft mc;

    @Inject(method = "renderModDebugOverlay", at = @At("RETURN"))
    private void renderModDebugOverlay(CallbackInfo info) {
        addPositionDisplay();
    }

    private void addPositionDisplay() {
        this.mc.fontRenderer.drawStringWithShadow(
                "XYZ: " + String.format("%.3f", this.mc.thePlayer.posX) + " / " +
                        String.format("%.3f", this.mc.thePlayer.boundingBox.minY) + " / " +
                        String.format("%.3f", this.mc.thePlayer.posZ), 2, 52, 16777215);
    }
}
