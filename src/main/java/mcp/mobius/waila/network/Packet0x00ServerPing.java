 package mcp.mobius.waila.network;

 import net.minecraft.src.Packet250CustomPayload;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.DataInputStream;
 import java.io.DataOutputStream;
 import java.io.IOException;


 public class Packet0x00ServerPing
 {
   public byte header;

   public Packet0x00ServerPing(Packet250CustomPayload packet) {
     DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
     try {
       this.header = inputStream.readByte();
     } catch (IOException ignored) {}
   }

   public static Packet250CustomPayload create() {
     Packet250CustomPayload packet = new Packet250CustomPayload();
     ByteArrayOutputStream bos = new ByteArrayOutputStream(1);
     DataOutputStream outputStream = new DataOutputStream(bos);

     try {
       outputStream.writeByte(0);
     } catch (IOException ignored) {}

     packet.channel = "Waila";
     packet.data = bos.toByteArray();
     packet.length = bos.size();

     return packet;
   }
 }


