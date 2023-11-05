 package mcp.mobius.waila.network;

 import net.minecraft.src.World;
 import net.minecraft.src.MovingObjectPosition;
 import net.minecraft.src.Packet250CustomPayload;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.DataInputStream;
 import java.io.DataOutputStream;
 import java.io.IOException;


 public class Packet0x01TERequest
 {
   public byte header;
   public int worldID;
   public int posX;
   public int posY;
   public int posZ;

   public Packet0x01TERequest(Packet250CustomPayload packet) {
     DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
     try {
       this.header = inputStream.readByte();
       this.worldID = inputStream.readInt();
       this.posX = inputStream.readInt();
       this.posY = inputStream.readInt();
       this.posZ = inputStream.readInt();
     } catch (IOException ignored) {}
   }

   public static Packet250CustomPayload create(World world, MovingObjectPosition mop) {
     Packet250CustomPayload packet = new Packet250CustomPayload();
     ByteArrayOutputStream bos = new ByteArrayOutputStream(17);
     DataOutputStream outputStream = new DataOutputStream(bos);

     try {
       outputStream.writeByte(1);
       outputStream.writeInt(world.getWorldInfo().getDimension());
       outputStream.writeInt(mop.blockX);
       outputStream.writeInt(mop.blockY);
       outputStream.writeInt(mop.blockZ);
     } catch (IOException ignored) {}

     packet.channel = "Waila";
     packet.data = bos.toByteArray();
     packet.length = bos.size();

     return packet;
   }
 }


