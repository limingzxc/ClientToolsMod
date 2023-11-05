package au.com.bytecode.opencsv.bean;

import au.com.bytecode.opencsv.CSVReader;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvToBean<T> {
  private Map<Class<?>, PropertyEditor> editorMap = null;
  
  public List<T> parse(MappingStrategy<T> paramMappingStrategy, Reader paramReader) {
    return parse(paramMappingStrategy, new CSVReader(paramReader));
  }
  
  public List<T> parse(MappingStrategy<T> paramMappingStrategy, CSVReader paramCSVReader) {
    try {
      paramMappingStrategy.captureHeader(paramCSVReader);
      ArrayList<T> arrayList = new ArrayList();
      String[] arrayOfString;
      while (null != (arrayOfString = paramCSVReader.readNext())) {
        T t = processLine(paramMappingStrategy, arrayOfString);
        arrayList.add(t);
      } 
      return arrayList;
    } catch (Exception exception) {
      throw new RuntimeException("Error parsing CSV!", exception);
    } 
  }
  
  protected T processLine(MappingStrategy<T> paramMappingStrategy, String[] paramArrayOfString) throws IllegalAccessException, InvocationTargetException, InstantiationException, IntrospectionException {
    T t = paramMappingStrategy.createBean();
    for (byte b = 0; b < paramArrayOfString.length; b++) {
      PropertyDescriptor propertyDescriptor = paramMappingStrategy.findDescriptor(b);
      if (null != propertyDescriptor) {
        String str = checkForTrim(paramArrayOfString[b], propertyDescriptor);
        Object object = convertValue(str, propertyDescriptor);
        propertyDescriptor.getWriteMethod().invoke(t, new Object[] { object });
      } 
    } 
    return t;
  }
  
  private String checkForTrim(String paramString, PropertyDescriptor paramPropertyDescriptor) {
    return trimmableProperty(paramPropertyDescriptor) ? paramString.trim() : paramString;
  }
  
  private boolean trimmableProperty(PropertyDescriptor paramPropertyDescriptor) {
    return !paramPropertyDescriptor.getPropertyType().getName().contains("String");
  }
  
  protected Object convertValue(String paramString, PropertyDescriptor paramPropertyDescriptor) throws InstantiationException, IllegalAccessException {
    PropertyEditor propertyEditor = getPropertyEditor(paramPropertyDescriptor);
    Object object = paramString;
    if (null != propertyEditor) {
      propertyEditor.setAsText(paramString);
      object = propertyEditor.getValue();
    } 
    return object;
  }
  
  private PropertyEditor getPropertyEditorValue(Class<?> paramClass) {
    if (this.editorMap == null)
      this.editorMap = new HashMap<Class<?>, PropertyEditor>(); 
    PropertyEditor propertyEditor = this.editorMap.get(paramClass);
    if (propertyEditor == null) {
      propertyEditor = PropertyEditorManager.findEditor(paramClass);
      addEditorToMap(paramClass, propertyEditor);
    } 
    return propertyEditor;
  }
  
  private void addEditorToMap(Class<?> paramClass, PropertyEditor paramPropertyEditor) {
    if (paramPropertyEditor != null)
      this.editorMap.put(paramClass, paramPropertyEditor); 
  }
  
  protected PropertyEditor getPropertyEditor(PropertyDescriptor paramPropertyDescriptor) throws InstantiationException, IllegalAccessException {
    Class<?> clazz = paramPropertyDescriptor.getPropertyEditorClass();
    return (null != clazz) ? (PropertyEditor)clazz.newInstance() : getPropertyEditorValue(paramPropertyDescriptor.getPropertyType());
  }
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\bean\CsvToBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */