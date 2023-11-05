package mcp.mobius.waila.api;

import net.minecraft.src.Block;

public interface IWailaRegistrar {
  void addConfig(String paramString1, String paramString2, String paramString3);
  
  void addConfigRemote(String paramString1, String paramString2, String paramString3);
  
  void registerHeadProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);
  
  void registerBodyProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);
  
  void registerStackProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);
  
  void registerStackProvider(IWailaDataProvider paramIWailaDataProvider, Class<?> paramClass);
  
  void registerHeadProvider(IWailaDataProvider paramIWailaDataProvider, Class<?> paramClass);
  
  void registerBodyProvider(IWailaDataProvider paramIWailaDataProvider, Class<?> paramClass);
  
  void registerDocTextFile(String paramString);
  
  void registerShortDataProvider(IWailaSummaryProvider paramIWailaSummaryProvider, Class<?> paramClass);
}
