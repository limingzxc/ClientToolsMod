package au.com.bytecode.opencsv.bean;

import java.util.HashMap;
import java.util.Map;

public class HeaderColumnNameTranslateMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {
  private Map<String, String> columnMapping = new HashMap<String, String>();
  
  protected String getColumnName(int paramInt) {
    return (paramInt < this.header.length) ? this.columnMapping.get(this.header[paramInt].toUpperCase()) : null;
  }
  
  public Map<String, String> getColumnMapping() {
    return this.columnMapping;
  }
  
  public void setColumnMapping(Map<String, String> paramMap) {
    for (String str : paramMap.keySet())
      this.columnMapping.put(str.toUpperCase(), paramMap.get(str)); 
  }
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\bean\HeaderColumnNameTranslateMappingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */