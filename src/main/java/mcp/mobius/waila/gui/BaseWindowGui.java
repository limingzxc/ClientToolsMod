package mcp.mobius.waila.gui;

import net.fabricmc.clienttools.lib.ItemInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Arrays;
import java.util.List;

public class BaseWindowGui extends Gui {

    private Minecraft theGame = Minecraft.getMinecraft();
    private int WindowWidth;
    private int WindowHeight;

    private RenderItem renderItem;

    private void updateAchievementWindowScale()
    {
        GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.WindowWidth = this.theGame.displayWidth;
        this.WindowHeight = this.theGame.displayHeight;
        ScaledResolution var1 = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
        this.WindowWidth = var1.getScaledWidth();
        this.WindowHeight = var1.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, this.WindowWidth, this.WindowHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    public void updateAchievementWindow() {

        List<String> info = ItemInfo.getText(theGame.thePlayer.getHeldItem(), theGame.theWorld, theGame.thePlayer, theGame.objectMouseOver);

        Item item = Item.diamond;
        int[] itemId = new int[0];

        if (info.size() > 1) {

            if (info.get(0).split(":")[0].equals("0")) {
                return;
            }

            itemId = Arrays.stream(info.get(0).split(":")).mapToInt(Integer::parseInt).toArray();

            item = Item.itemsList[itemId[0]];

            info.add(0, item.getItemDisplayName(new ItemStack(item)));
        }
        if(info.size() != 0)
        {
            info.remove(info.size() - 1);
        }

        if (info.size() == 0 || theGame.isGamePaused || theGame.thePlayer.isDead ||
                theGame.thePlayer.openContainer != theGame.thePlayer.inventoryContainer ||
                theGame.thePlayer.isPlayerSleeping()) {
            return;
        }

        this.updateAchievementWindowScale();
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
//        GL11.glDepthMask(false);
        int var5 = this.WindowWidth / 2 - 80;
        int var6 = 0;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.theGame.renderEngine.bindTexture("/achievement/bg.png");
        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawTexturedModalRect(var5, var6, 96, 202, 160, 32);

        this.theGame.fontRenderer.drawSplitString(String.valueOf(info), var5 + 30, var6 + 7, 120, -1);

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        // new RenderBlocks().renderBlockAsItemVanilla(new Block());
        if (renderItem == null) {
            renderItem = new RenderItem();
        }
        renderItem.renderItemIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, new ItemStack(item, 1, itemId[1]), var5 + 8, var6 + 8);
        GL11.glDisable(GL11.GL_LIGHTING);
        // GL11.glDepthMask(true);
        // GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
