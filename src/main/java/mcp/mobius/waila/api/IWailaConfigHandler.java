package mcp.mobius.waila.api;

import java.util.HashMap;
import java.util.Set;

public interface IWailaConfigHandler {
  Set<String> getModuleNames();
  
  HashMap<String, String> getConfigKeys(String paramString);
  
  boolean getConfig(String paramString, boolean paramBoolean);
  
  boolean getConfig(String paramString);
}
