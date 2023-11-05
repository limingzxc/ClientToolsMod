 package mcp.mobius.waila.network;

 import net.fabricmc.clienttools.lib.IConnectionHandler;
 import net.fabricmc.clienttools.lib.PacketDispatcher;
 import net.fabricmc.clienttools.lib.Player;
 import net.minecraft.server.MinecraftServer;
 import net.minecraft.src.INetworkManager;
 import net.minecraft.src.NetHandler;
 import net.minecraft.src.NetLoginHandler;
 import net.minecraft.src.Packet1Login;

 public class WailaConnectionHandler implements IConnectionHandler {
   public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
     PacketDispatcher.sendPacketToPlayer( Packet0x00ServerPing.create(), player);
   }


   public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
     return null;
   }

   public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {}

   public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}

   public void connectionClosed(INetworkManager manager) {}

   public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {}
 }


