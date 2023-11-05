package net.fabricmc.clienttools.lib;

import btw.community.clienttools.ClientToolsAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.server.MinecraftServer;

/**
 * A simple utility class to send packet 250 packets around the place
 *
 * @author cpw
 *
 */
public class PacketDispatcher
{
    public static Packet250CustomPayload getPacket(String type, byte[] data)
    {
        return new Packet250CustomPayload(type, data);
    }

    public static void sendPacketToServer(Packet packet)
    {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }

    public static void sendPacketToPlayer(Packet packet, Player player)
    {
        if (player instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(packet);
        }
    }

    public static void sendPacketToAllAround(double X, double Y, double Z, double range, int dimensionId, Packet packet)
    {
        MinecraftServer server = MinecraftServer.getServer();
        if (server != null)
        {
            server.getConfigurationManager().sendToAllNear(X, Y, Z, range, dimensionId, packet);
        }
        else
        {
            ClientToolsAddon.ModLogger("Attempt to send packet to all around without a server instance available");
        }
    }

    public static void sendPacketToAllInDimension(Packet packet, int dimId)
    {
        MinecraftServer server = MinecraftServer.getServer();
        if (server != null)
        {
            server.getConfigurationManager().sendPacketToAllPlayersInDimension(packet, dimId);
        }
        else
        {
            ClientToolsAddon.ModLogger("Attempt to send packet to all in dimension without a server instance available");
        }
    }

    public static void sendPacketToAllPlayers(Packet packet)
    {
        MinecraftServer server = MinecraftServer.getServer();
        if (server != null)
        {
            server.getConfigurationManager().sendPacketToAllPlayers(packet);
        }
        else
        {
            ClientToolsAddon.ModLogger("Attempt to send packet to all in dimension without a server instance available");
        }
    }
}

