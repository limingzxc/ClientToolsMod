package au.com.bytecode.opencsv.bean;

import au.com.bytecode.opencsv.CSVReader;
import java.io.IOException;

public class ColumnPositionMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {
  private String[] columnMapping = new String[0];
  
  public void captureHeader(CSVReader paramCSVReader) throws IOException {}
  
  protected String getColumnName(int paramInt) {
    return (null != this.columnMapping && paramInt < this.columnMapping.length) ? this.columnMapping[paramInt] : null;
  }
  
  public String[] getColumnMapping() {
    return (this.columnMapping != null) ? (String[])this.columnMapping.clone() : null;
  }
  
  public void setColumnMapping(String[] paramArrayOfString) {
    this.columnMapping = (paramArrayOfString != null) ? (String[])paramArrayOfString.clone() : null;
  }
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\bean\ColumnPositionMappingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */