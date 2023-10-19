package net.fabricmc.clienttools.mixin;

import net.fabricmc.clienttools.AutoClick;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow public PlayerControllerMP playerController;

    @Shadow private int rightClickDelayTimer;

    @Shadow @Final public Profiler mcProfiler;

    @Shadow public StatFileWriter statFileWriter;

    @Shadow public volatile boolean isGamePaused;

    @Shadow public GuiIngame ingameGUI;

    @Shadow public EntityRenderer entityRenderer;

    @Shadow public WorldClient theWorld;

    @Shadow public RenderEngine renderEngine;

    @Shadow public GuiScreen currentScreen;

    @Shadow public EntityClientPlayerMP thePlayer;

    @Shadow public abstract void displayGuiScreen(GuiScreen par1GuiScreen);

    @Shadow private int leftClickCounter;

    @Shadow @Final public static boolean isRunningOnMac;

    @Shadow long systemTime;

    @Shadow public GameSettings gameSettings;

    @Shadow public boolean inGameHasFocus;

    @Shadow public abstract void setIngameFocus();

    @Shadow private long field_83002_am;

    @Shadow public abstract void toggleFullscreen();

    @Shadow public abstract void displayInGameMenu();

    @Shadow protected abstract void forceReload();

    @Shadow public RenderGlobal renderGlobal;

    @Shadow protected abstract void updateDebugProfilerName(int par1);

    @Shadow protected abstract void clickMouse(int par1);

    @Shadow protected abstract void clickMiddleMouseButton();

    @Shadow protected abstract void sendClickBlockToController(int par1, boolean par2);

    @Shadow private int joinPlayerCounter;

    @Shadow public EffectRenderer effectRenderer;

    @Shadow private INetworkManager myNetworkManager;


    @Shadow public MovingObjectPosition objectMouseOver;

    @Shadow public abstract ILogAgent getLogAgent();

    private AutoClick playerControllerTool;


    /**
     * @author limingzxc
     * @reason Expand auto-pounding block functionality
     */
    @Overwrite
    public void runTick() {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }

        this.mcProfiler.startSection("stats");
        this.statFileWriter.func_77449_e();
        this.mcProfiler.endStartSection("gui");

        if (!this.isGamePaused) {
            this.ingameGUI.updateTick();
        }

        this.mcProfiler.endStartSection("pick");
        this.entityRenderer.getMouseOver(1.0F);
        this.mcProfiler.endStartSection("gameMode");

        if (!this.isGamePaused && this.theWorld != null) {
            this.playerController.updateController();
        }

        this.renderEngine.bindTexture("/terrain.png");
        this.mcProfiler.endStartSection("textures");

        if (!this.isGamePaused) {
            this.renderEngine.updateDynamicTextures();
        }

        if (this.currentScreen == null && this.thePlayer != null) {
            if (this.thePlayer.getHealth() <= 0) {
                this.displayGuiScreen((GuiScreen) null);
            } else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
            this.displayGuiScreen((GuiScreen) null);
        }

        if (this.currentScreen != null) {
            this.leftClickCounter = 10000;
        }

        CrashReport var2;
        CrashReportCategory var3;

        if (this.currentScreen != null) {
            try {
                this.currentScreen.handleInput();
            } catch (Throwable var6) {
                var2 = CrashReport.makeCrashReport(var6, "Updating screen events");
                var3 = var2.makeCategory("Affected screen");
                var3.addCrashSectionCallable("Screen name", new CallableUpdatingScreenName(Minecraft.getMinecraft()));
                throw new ReportedException(var2);
            }

            if (this.currentScreen != null) {
                try {
                    this.currentScreen.guiParticles.update();
                } catch (Throwable var5) {
                    var2 = CrashReport.makeCrashReport(var5, "Ticking screen particles");
                    var3 = var2.makeCategory("Affected screen");
                    var3.addCrashSectionCallable("Screen name", new CallableParticleScreenName(Minecraft.getMinecraft()));
                    throw new ReportedException(var2);
                }

                try {
                    this.currentScreen.updateScreen();
                } catch (Throwable var4) {
                    var2 = CrashReport.makeCrashReport(var4, "Ticking screen");
                    var3 = var2.makeCategory("Affected screen");
                    var3.addCrashSectionCallable("Screen name", new CallableTickingScreenName(Minecraft.getMinecraft()));
                    throw new ReportedException(var2);
                }
            }
        }

        if (this.currentScreen == null || this.currentScreen.allowUserInput) {
            this.mcProfiler.endStartSection("mouse");

            while (Mouse.next()) {
                int var1_1 = Mouse.getEventButton();

                if (isRunningOnMac && var1_1 == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157))) {
                    var1_1 = 1;
                }

                KeyBinding.setKeyBindState(var1_1 - 100, Mouse.getEventButtonState());

                if (Mouse.getEventButtonState()) {
                    KeyBinding.onTick(var1_1 - 100);
                }

                long var1 = Minecraft.getSystemTime() - this.systemTime;

                if (var1 <= 200L) {
                    int var10 = Mouse.getEventDWheel();

                    if (var10 != 0) {
                        this.thePlayer.inventory.changeCurrentItem(var10);

                        if (this.gameSettings.noclip) {
                            if (var10 > 0) {
                                var10 = 1;
                            }

                            if (var10 < 0) {
                                var10 = -1;
                            }

                            this.gameSettings.noclipRate += (float) var10 * 0.25F;
                        }
                    }

                    if (this.currentScreen == null) {
                        if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
                            this.setIngameFocus();
                        }
                    } else if (this.currentScreen != null) {
                        this.currentScreen.handleMouseInput();
                    }
                }
            }

            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }

            this.mcProfiler.endStartSection("keyboard");
            boolean var8;

            while (Keyboard.next()) {
                KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());

                if (Keyboard.getEventKeyState()) {
                    KeyBinding.onTick(Keyboard.getEventKey());
                }

                if (this.field_83002_am > 0L) {
                    if (Minecraft.getSystemTime() - this.field_83002_am >= 6000L) {
                        throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                    }

                    if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                        this.field_83002_am = -1L;
                    }
                } else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
                    this.field_83002_am = Minecraft.getSystemTime();
                }

                if (Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == 87) {
                        this.toggleFullscreen();
                    } else {
                        if (this.currentScreen != null) {
                            this.currentScreen.handleKeyboardInput();
                        } else {
                            if (Keyboard.getEventKey() == 1) {
                                this.displayInGameMenu();
                            }

                            if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
                                this.forceReload();
                            }

                            if (Keyboard.getEventKey() == 20 && Keyboard.isKeyDown(61)) {
                                this.renderEngine.refreshTextures();
                                this.renderGlobal.loadRenderers();
                            }

                            if (Keyboard.getEventKey() == 33 && Keyboard.isKeyDown(61)) {
                                var8 = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
                                this.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, var8 ? -1 : 1);
                            }

                            if (Keyboard.getEventKey() == 30 && Keyboard.isKeyDown(61)) {
                                this.renderGlobal.loadRenderers();
                            }

                            if (Keyboard.getEventKey() == 35 && Keyboard.isKeyDown(61)) {
                                this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                                this.gameSettings.saveOptions();
                            }

                            if (Keyboard.getEventKey() == 48 && Keyboard.isKeyDown(61)) {
                                RenderManager.field_85095_o = !RenderManager.field_85095_o;
                            }

                            if (Keyboard.getEventKey() == 25 && Keyboard.isKeyDown(61)) {
                                this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                                this.gameSettings.saveOptions();
                            }

                            if (Keyboard.getEventKey() == 59) {
                                this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                            }

                            if (Keyboard.getEventKey() == 61) {
                                this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                                this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                            }

                            if (Keyboard.getEventKey() == 63) {
                                ++this.gameSettings.thirdPersonView;

                                if (this.gameSettings.thirdPersonView > 2) {
                                    this.gameSettings.thirdPersonView = 0;
                                }
                            }

                            if (Keyboard.getEventKey() == 66) {
                                this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                            }
                        }

                        int var9;

                        for (var9 = 0; var9 < 9; ++var9) {
                            if (Keyboard.getEventKey() == 2 + var9) {
                                this.thePlayer.inventory.currentItem = var9;
                            }
                        }

                        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart) {
                            if (Keyboard.getEventKey() == 11) {
                                this.updateDebugProfilerName(0);
                            }

                            for (var9 = 0; var9 < 9; ++var9) {
                                if (Keyboard.getEventKey() == 2 + var9) {
                                    this.updateDebugProfilerName(var9 + 1);
                                }
                            }
                        }
                    }
                }
            }

            var8 = this.gameSettings.chatVisibility != 2;

            while (this.gameSettings.keyBindInventory.isPressed()) {
                this.displayGuiScreen(new GuiInventory(this.thePlayer));
            }

            while (this.gameSettings.keyBindDrop.isPressed()) {
                this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
            }

            while (this.gameSettings.keyBindChat.isPressed() && var8) {
                this.displayGuiScreen(new GuiChat());
            }

            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && var8) {
                this.displayGuiScreen(new GuiChat("/"));
            }

            if (this.thePlayer.isUsingItem()) {
                if (!this.gameSettings.keyBindUseItem.pressed) {
                    this.playerController.onStoppedUsingItem(this.thePlayer);
                }

                label379:

                while (true) {
                    if (!this.gameSettings.keyBindAttack.isPressed()) {
                        while (this.gameSettings.keyBindUseItem.isPressed()) {
                            ;
                        }

                        while (true) {
                            if (this.gameSettings.keyBindPickBlock.isPressed()) {
                                continue;
                            }

                            break label379;
                        }
                    }
                }
            } else {
                while (this.gameSettings.keyBindAttack.isPressed()) {
                    this.clickMouse(0);
                }

                while (this.gameSettings.keyBindUseItem.isPressed()) {
                    this.clickMouse(1);
                }

                while (this.gameSettings.keyBindPickBlock.isPressed()) {
                    this.clickMiddleMouseButton();
                }
            }

            this.playerControllerTool = ((AutoClick) this.playerController);

            if (this.playerControllerTool.getAutoHarvestBlock() != null && this.playerControllerTool.autoHarvestModeHasExpired()) {
                this.playerControllerTool.clearAutoHarvestMode();
            }

            if (!(this.thePlayer.isUsingItem() || !this.gameSettings.keyBindAttack.pressed && this.gameSettings.keyBindUseItem.pressed)) {
                this.sendClickBlockToController(0, !this.gameSettings.hideGUI && (this.gameSettings.keyBindAttack.pressed
                        || this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE
                        && this.playerControllerTool.matchesAutoHarvestBlock(this.objectMouseOver.blockX,
                        this.objectMouseOver.blockY, this.objectMouseOver.blockZ)) && this.inGameHasFocus);
            }

            if (this.playerControllerTool.getListeningForAutoHarvestModeClick()) {
                this.getLogAgent().logSevere("Listening for both AHM clicks");
            }

            if (this.gameSettings.keyBindAttack.pressed && this.gameSettings.keyBindUseItem.pressed) {

                if (this.playerControllerTool.getListeningForAutoHarvestModeClick() &&
                        this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
                    this.playerControllerTool.setAutoHarvestMode(this.objectMouseOver.blockX,
                            this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
                }

            } else if (this.gameSettings.keyBindAttack.pressed) {

                if (this.playerControllerTool.getCancelAutoHarvestOnNextClick()) {
                    this.playerControllerTool.clearAutoHarvestMode();
                } else {
                    this.playerControllerTool.setListeningForAutoHarvestMode();
                }

            } else if (this.gameSettings.keyBindUseItem.pressed && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem()) {
                // FCMOD: Code added (Client Only)
                ItemStack currentStack = thePlayer.inventory.getCurrentItem();

                if (currentStack != null && currentStack.getItem().isMultiUsePerClick())
                    // END FCMOD
                    this.clickMouse(1);
            }

            if (!this.gameSettings.keyBindAttack.pressed) {
                this.leftClickCounter = 0;
                this.playerControllerTool.setCancelAutoHarvestOnNextClick(true);
            }
        }


        if (this.theWorld != null) {
            if (this.thePlayer != null) {
                ++this.joinPlayerCounter;

                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings(this.thePlayer);
                }
            }

            this.mcProfiler.endStartSection("gameRenderer");

            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }

            this.mcProfiler.endStartSection("levelRenderer");

            if (!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }

            this.mcProfiler.endStartSection("level");

            if (!this.isGamePaused) {
                if (this.theWorld.lastLightningBolt > 0) {
                    --this.theWorld.lastLightningBolt;
                }

                this.theWorld.updateEntities();
            }

            if (!this.isGamePaused) {
                this.theWorld.setAllowedSpawnTypes(this.theWorld.difficultySetting > 0, true);

                try {
                    this.theWorld.tick();
                } catch (Throwable var7) {
                    var2 = CrashReport.makeCrashReport(var7, "Exception in world tick");

                    if (this.theWorld == null) {
                        var3 = var2.makeCategory("Affected level");
                        var3.addCrashSection("Problem", "Level is null!");
                    } else {
                        this.theWorld.addWorldInfoToCrashReport(var2);
                    }

                    throw new ReportedException(var2);
                }
            }

            this.mcProfiler.endStartSection("animateTick");

            if (!this.isGamePaused && this.theWorld != null) {
                this.theWorld.doVoidFogParticles(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
            }

            this.mcProfiler.endStartSection("particles");

            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        } else if (this.myNetworkManager != null) {
            this.mcProfiler.endStartSection("pendingConnection");
            this.myNetworkManager.processReadPackets();
        }

        this.mcProfiler.endSection();
        this.systemTime = Minecraft.getSystemTime();

    }


    @Inject(method = "sendClickBlockToController", at = @At("HEAD"))
    private void sendClickBlockToController(int par1, boolean par2, CallbackInfo info) {
        if (par1 == 0 && par2 && this.playerControllerTool != null) {
            this.playerControllerTool.setLastAutoHarvestMs(System.currentTimeMillis());
        }
    }
}
