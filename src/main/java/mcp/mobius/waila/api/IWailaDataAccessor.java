package mcp.mobius.waila.api;

import net.minecraft.src.World;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.EntityPlayer;

public interface IWailaDataAccessor {

  World getWorld();

  EntityPlayer getPlayer();

  Block getBlock();

  int getBlockID();

  int getMetadata();

  TileEntity getTileEntity();

  MovingObjectPosition getPosition();

  NBTTagCompound getNBTData();

  int getNBTInteger(NBTTagCompound tag, String key);

}

