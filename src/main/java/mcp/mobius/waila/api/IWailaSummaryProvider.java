package mcp.mobius.waila.api;

import java.util.LinkedHashMap;
import net.minecraft.src.ItemStack;

public interface IWailaSummaryProvider {
  LinkedHashMap<String, String> getSummary(ItemStack paramwm, LinkedHashMap<String, String> paramLinkedHashMap, IWailaConfigHandler paramIWailaConfigHandler);
}

