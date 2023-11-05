package net.fabricmc.clienttools.lib;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

import java.util.ArrayList;
import java.util.List;

public class ItemInfo {
    public static enum Layout
    {
        HEADER, BODY, FOOTER
    }

    public static final ArrayListMultimap<Layout, IHighlightHandler> highlightHandlers = ArrayListMultimap.create();

    public static void registerHighlightHandler(IHighlightHandler handler, ItemInfo.Layout... layouts) {
        for (ItemInfo.Layout layout : layouts)
            ItemInfo.highlightHandlers.get(layout).add(handler);
    }

    public static List<String> getText(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop) {
        List<String> retString = new ArrayList<>();

        for (ItemInfo.Layout layout : ItemInfo.Layout.values())
            for (IHighlightHandler handler : ItemInfo.highlightHandlers.get(layout))
                retString = handler.handleTextData(itemStack, world, player, mop, retString, layout);

        return retString;
    }
}
