 package mcp.mobius.waila.network;

 import btw.community.clienttools.ClientToolsAddon;
 import mcp.mobius.waila.WailaExceptionHandler;
 import mcp.mobius.waila.handlers.DataAccessor;
 import net.fabricmc.clienttools.lib.IPacketHandler;
 import net.fabricmc.clienttools.lib.PacketDispatcher;
 import net.fabricmc.clienttools.lib.Player;
 import net.minecraft.server.MinecraftServer;
 import net.minecraft.src.INetworkManager;
 import net.minecraft.src.NBTTagCompound;
 import net.minecraft.src.Packet250CustomPayload;
 import net.minecraft.src.TileEntity;

 import java.io.ByteArrayInputStream;
 import java.io.DataInputStream;
 import java.io.IOException;

 public class WailaPacketHandler implements IPacketHandler
 {
   public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
     if (packet.channel.equals("Waila")) {
       try {
         byte header = getHeader(packet);

         if (header == 0) {
             ClientToolsAddon.ModLogger("Received server authentication msg. Remote sync will be activated");
           ClientToolsAddon.instance.serverPresent = true;
         }
         else if (header == 1) {
           Packet0x01TERequest castedPacket = new Packet0x01TERequest(packet);
           MinecraftServer server = MinecraftServer.getServer();
           TileEntity entity = server.worldServers[castedPacket.worldID].getBlockTileEntity(castedPacket.posX, castedPacket.posY, castedPacket.posZ);

           if (entity != null) {
             try {
               NBTTagCompound tag = new NBTTagCompound();
               entity.writeToNBT(tag);
               PacketDispatcher.sendPacketToPlayer(Packet0x02TENBTData.create(tag), player);
             } catch (Throwable e) {
               WailaExceptionHandler.handleErr(e, entity.getClass().toString(), null);
             }

           }
         } else if (header == 2) {
           Packet0x02TENBTData castedPacket = new Packet0x02TENBTData(packet);
           DataAccessor.instance.remoteNbt = castedPacket.tag;
         }

       } catch (Exception ignored) {
       }
     }
   }

   public byte getHeader(Packet250CustomPayload packet) {
     DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
     try {
       return inputStream.readByte();
     } catch (IOException e) {
       return -1;
     }
   }
 }


