package au.com.bytecode.opencsv.bean;

import au.com.bytecode.opencsv.CSVReader;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HeaderColumnNameMappingStrategy<T> implements MappingStrategy<T> {
  protected String[] header;
  
  protected Map<String, PropertyDescriptor> descriptorMap = null;
  
  protected Class<T> type;
  
  public void captureHeader(CSVReader paramCSVReader) throws IOException {
    this.header = paramCSVReader.readNext();
  }
  
  public PropertyDescriptor findDescriptor(int paramInt) throws IntrospectionException {
    String str = getColumnName(paramInt);
    return (null != str && str.trim().length() > 0) ? findDescriptor(str) : null;
  }
  
  protected String getColumnName(int paramInt) {
    return (null != this.header && paramInt < this.header.length) ? this.header[paramInt] : null;
  }
  
  protected PropertyDescriptor findDescriptor(String paramString) throws IntrospectionException {
    if (null == this.descriptorMap)
      this.descriptorMap = loadDescriptorMap(getType()); 
    return this.descriptorMap.get(paramString.toUpperCase().trim());
  }
  
  protected boolean matches(String paramString, PropertyDescriptor paramPropertyDescriptor) {
    return paramPropertyDescriptor.getName().equals(paramString.trim());
  }
  
  protected Map<String, PropertyDescriptor> loadDescriptorMap(Class<T> paramClass) throws IntrospectionException {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    PropertyDescriptor[] arrayOfPropertyDescriptor = loadDescriptors(getType());
    for (PropertyDescriptor propertyDescriptor : arrayOfPropertyDescriptor)
      hashMap.put(propertyDescriptor.getName().toUpperCase().trim(), propertyDescriptor); 
    return (Map)hashMap;
  }
  
  private PropertyDescriptor[] loadDescriptors(Class<T> paramClass) throws IntrospectionException {
    BeanInfo beanInfo = Introspector.getBeanInfo(paramClass);
    return beanInfo.getPropertyDescriptors();
  }
  
  public T createBean() throws InstantiationException, IllegalAccessException {
    return this.type.newInstance();
  }
  
  public Class<T> getType() {
    return this.type;
  }
  
  public void setType(Class<T> paramClass) {
    this.type = paramClass;
  }
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\bean\HeaderColumnNameMappingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */