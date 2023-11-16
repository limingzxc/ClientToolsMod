package net.fabricmc.clienttools.mixin;

import net.fabricmc.clienttools.api.PinyinMatch;
import net.minecraft.src.GuiContainerCreative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiContainerCreative.class)
public abstract class GuiContainerCreativeMixin {
    @Redirect(method = "updateCreativeSearch", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"))
    private boolean redirectContains(String haystack, CharSequence needle) {
        return PinyinMatch.contains(haystack, needle);
    }
}
