package au.com.bytecode.opencsv.bean;

import au.com.bytecode.opencsv.CSVReader;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;

public interface MappingStrategy<T> {
  PropertyDescriptor findDescriptor(int paramInt) throws IntrospectionException;
  
  T createBean() throws InstantiationException, IllegalAccessException;
  
  void captureHeader(CSVReader paramCSVReader) throws IOException;
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\bean\MappingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */