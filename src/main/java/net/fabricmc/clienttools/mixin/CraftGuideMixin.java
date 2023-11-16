package net.fabricmc.clienttools.mixin;

import net.fabricmc.clienttools.api.PinyinMatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import uristqwerty.CraftGuide.CommonUtilities;

@Mixin(value = CommonUtilities.class, remap = false)
public abstract class CraftGuideMixin {
    @Redirect(method = "searchExtendedItemStackText", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"), remap = false)
    private static boolean redirectContains(String haystack, CharSequence needle) {
        return PinyinMatch.contains(haystack, needle);
    }
}
