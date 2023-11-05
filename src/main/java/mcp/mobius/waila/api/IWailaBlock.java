package mcp.mobius.waila.api;

import java.util.List;
import net.minecraft.src.ItemStack;

public interface IWailaBlock {
  ItemStack getWailaStack(IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler);
  
  List<String> getWailaHead(ItemStack paramwm, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler);
  
  List<String> getWailaBody(ItemStack paramwm, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler);
}
