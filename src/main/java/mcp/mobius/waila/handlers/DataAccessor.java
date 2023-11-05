 package mcp.mobius.waila.handlers;

 import net.minecraft.src.*;
 import mcp.mobius.waila.api.IWailaDataAccessor;


 public class DataAccessor implements IWailaDataAccessor
 {
   public World world;
   public EntityPlayer player;
   public MovingObjectPosition mop;
   public Block block;
   public int blockID;
   public int metadata;
   public TileEntity entity;
   public NBTTagCompound remoteNbt = null;
   public long timeLastUpdate = System.currentTimeMillis();

   public static DataAccessor instance = new DataAccessor();

   public void set(World _world, EntityPlayer _player, MovingObjectPosition _mop) {
     this.world = _world;
     this.player = _player;
     this.mop = _mop;
     if(_mop == null) {
       this.blockID = 0;
       this.block = Block.blocksList[this.blockID];
       this.metadata = 0;
       this.entity = null;
     } else {
       this.blockID = this.world.getBlockId(_mop.blockX, _mop.blockY, _mop.blockZ);
       this.block = Block.blocksList[this.blockID];
       this.metadata = this.world.getBlockMetadata(_mop.blockX, _mop.blockY, _mop.blockZ);
       this.entity = this.world.getBlockTileEntity(_mop.blockX, _mop.blockY, _mop.blockZ);
     }
   }


   public World getWorld() {
     return this.world;
   }


   public EntityPlayer getPlayer() {
     return this.player;
   }


   public Block getBlock() {
     return this.block;
   }


   public int getMetadata() {
     return this.metadata;
   }


   public TileEntity getTileEntity() {
     return this.entity;
   }


   public MovingObjectPosition getPosition() {
     return this.mop;
   }


   public NBTTagCompound getNBTData() {
     if (this.entity == null) return null;

     if (isTagCorrect(this.remoteNbt)) {
       return this.remoteNbt;
     }
     NBTTagCompound tag = new NBTTagCompound();
     this.entity.readFromNBT(tag);
     return tag;
   }


   public int getNBTInteger(NBTTagCompound tag, String keyname) {
     NBTBase subtag = tag.getTag(keyname);
     if (subtag instanceof NBTTagInt)
       return tag.getInteger(keyname);
     if (subtag instanceof NBTTagShort)
       return tag.getShort(keyname);
     if (subtag instanceof NBTTagByte) {
       return tag.getByte(keyname);
     }
     return 0;
   }


   public int getBlockID() {
     return this.blockID;
   }

   private boolean isTagCorrect(NBTTagCompound tag) {
     if (tag == null) {
       this.timeLastUpdate = System.currentTimeMillis() - 250L;
       return false;
     }

     int x = tag.getInteger("x");
     int y = tag.getInteger("y");
     int z = tag.getInteger("z");

     if (x == this.mop.blockX && y == this.mop.blockY && z == this.mop.blockZ) {
       return true;
     }
     this.timeLastUpdate = System.currentTimeMillis() - 250L;
     return false;
   }
 }


