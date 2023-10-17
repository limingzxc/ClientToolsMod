package net.fabricmc.clienttools.mixin;

import btw.block.blocks.BedBlockBase;
import net.fabricmc.clienttools.AutoClick;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPMixin implements AutoClick
{
    /** The Minecraft instance. */
    @Final @Shadow private Minecraft mc;
    public boolean listening_for_auto_harvest_mode_click;
    public Block auto_harvest_block;
    private int auto_harvest_block_metadata;
    public boolean cancel_auto_harvest_on_next_click;
    public long last_auto_harvest_ms;
    private static final Block[] blocks_for_which_metadata_must_match_for_automatic_harvest_mode = new Block[]{ Block.tallGrass };

    @Shadow public abstract void resetBlockRemoving();

    @Override
    public void setListeningForAutoHarvestMode() {
        this.listening_for_auto_harvest_mode_click = true;
        this.cancel_auto_harvest_on_next_click = false;
    }

    @Override
    public void setAutoHarvestMode(int x, int y, int z) {
        this.auto_harvest_block = Block.blocksList[this.mc.theWorld.getBlockId(x, y, z)];
        if (this.auto_harvest_block == null) {
            this.clearAutoHarvestMode();
            return;
        }
        this.auto_harvest_block_metadata = this.mc.theWorld.getBlockMetadata(x, y, z);
        this.cancel_auto_harvest_on_next_click = false;
        this.last_auto_harvest_ms = System.currentTimeMillis();
    }

    @Override
    public void clearAutoHarvestMode() {
        this.auto_harvest_block = null;
        this.auto_harvest_block_metadata = 0;
        this.cancel_auto_harvest_on_next_click = false;
        this.last_auto_harvest_ms = 0L;
        if (!this.mc.gameSettings.keyBindAttack.pressed) {
            this.resetBlockRemoving();
        }
    }

    @Override
    public boolean autoHarvestModeHasExpired() {
        return System.currentTimeMillis() - this.last_auto_harvest_ms > 5000L;
    }

    @Override
    public boolean matchesAutoHarvestBlock(int x, int y, int z) {
        if (this.auto_harvest_block == null) {
            return false;
        }
        if (this.mc.thePlayer.isDead || this.isInBed(mc.thePlayer) || this.autoHarvestModeHasExpired()) {
            this.clearAutoHarvestMode();
            return false;
        }
        Block block = Block.blocksList[this.mc.theWorld.getBlockId(x, y, z)];
        if (!(this.auto_harvest_block != Block.dirt && this.auto_harvest_block != Block.grass || block != Block.dirt && block != Block.grass)) {
            return true;
        }
        if (this.auto_harvest_block == Block.oreRedstoneGlowing && block == Block.oreRedstone) {
            return true;
        }
        if (block != this.auto_harvest_block) {
            return false;
        }
        int metadata = this.mc.theWorld.getBlockMetadata(x, y, z);
        if (metadata == this.auto_harvest_block_metadata) {
            return true;
        }
        for (Block value : blocks_for_which_metadata_must_match_for_automatic_harvest_mode) {
            if (value != block) continue;
            return false;
        }
        return true;
    }

    @Override
    public void setLastAutoHarvestMs(long last_auto_harvest_ms) {
        this.last_auto_harvest_ms = last_auto_harvest_ms;
    }

    @Override
    public boolean isInBed(EntityPlayer player) {
        World worldObj = player.worldObj;
        ChunkCoordinates playerLocation = player.playerLocation;
        if(playerLocation == null) {
            return false;
        }
        return Block.blocksList[worldObj.getBlockId(playerLocation.posX, playerLocation.posY, playerLocation.posZ)] instanceof BedBlockBase;
    }

    @Override
    public Block getAutoHarvestBlock() { return this.auto_harvest_block; }

    @Override
    public boolean getListeningForAutoHarvestModeClick() {
        return this.listening_for_auto_harvest_mode_click;
    }

    @Override
    public boolean getCancelAutoHarvestOnNextClick() {
        return this.cancel_auto_harvest_on_next_click;
    }

    @Override
    public void setCancelAutoHarvestOnNextClick(boolean cancel_auto_harvest_on_next_click) {
        this.cancel_auto_harvest_on_next_click = cancel_auto_harvest_on_next_click;
    }
}
