package net.fabricmc.clienttools.lib;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

import java.util.List;

public interface IHighlightHandler
{
    public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop);

    public List<String> handleTextData(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop, List<String> currenttip, ItemInfo.Layout layout);
}