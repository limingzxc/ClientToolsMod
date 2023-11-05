package net.fabricmc.clienttools.mixin;

import btw.block.tileentity.OvenTileEntity;
import net.fabricmc.clienttools.api.TileEntityDataAccessor;
import net.minecraft.src.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OvenTileEntity.class)
public abstract class OvenTileEntityMixin extends TileEntityFurnace implements TileEntityDataAccessor {
    @Shadow(remap=false) private int unlitFuelBurnTime;

    @Override
    public String getData() {
        return String.format("%s : %s", furnaceCookTime, unlitFuelBurnTime + furnaceBurnTime);
    }
}
