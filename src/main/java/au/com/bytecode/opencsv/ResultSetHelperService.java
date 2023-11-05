package au.com.bytecode.opencsv;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ResultSetHelperService implements ResultSetHelper {
  public static final int CLOBBUFFERSIZE = 2048;
  
  private static final int NVARCHAR = -9;
  
  private static final int NCHAR = -15;
  
  private static final int LONGNVARCHAR = -16;
  
  private static final int NCLOB = 2011;
  
  public String[] getColumnNames(ResultSet paramResultSet) throws SQLException {
    ArrayList<String> arrayList = new ArrayList();
    ResultSetMetaData resultSetMetaData = paramResultSet.getMetaData();
    for (byte b = 0; b < resultSetMetaData.getColumnCount(); b++)
      arrayList.add(resultSetMetaData.getColumnName(b + 1)); 
    String[] arrayOfString = new String[arrayList.size()];
    return arrayList.<String>toArray(arrayOfString);
  }
  
  public String[] getColumnValues(ResultSet paramResultSet) throws SQLException, IOException {
    ArrayList<String> arrayList = new ArrayList();
    ResultSetMetaData resultSetMetaData = paramResultSet.getMetaData();
    for (byte b = 0; b < resultSetMetaData.getColumnCount(); b++)
      arrayList.add(getColumnValue(paramResultSet, resultSetMetaData.getColumnType(b + 1), b + 1)); 
    String[] arrayOfString = new String[arrayList.size()];
    return arrayList.<String>toArray(arrayOfString);
  }
  
  private String handleObject(Object paramObject) {
    return (paramObject == null) ? "" : String.valueOf(paramObject);
  }
  
  private String handleBigDecimal(BigDecimal paramBigDecimal) {
    return (paramBigDecimal == null) ? "" : paramBigDecimal.toString();
  }
  
  private String handleLong(ResultSet paramResultSet, int paramInt) throws SQLException {
    long l = paramResultSet.getLong(paramInt);
    return paramResultSet.wasNull() ? "" : Long.toString(l);
  }
  
  private String handleInteger(ResultSet paramResultSet, int paramInt) throws SQLException {
    int i = paramResultSet.getInt(paramInt);
    return paramResultSet.wasNull() ? "" : Integer.toString(i);
  }
  
  private String handleDate(ResultSet paramResultSet, int paramInt) throws SQLException {
    Date date = paramResultSet.getDate(paramInt);
    String str = null;
    if (date != null) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
      str = simpleDateFormat.format(date);
    } 
    return str;
  }
  
  private String handleTime(Time paramTime) {
    return (paramTime == null) ? null : paramTime.toString();
  }
  
  private String handleTimestamp(Timestamp paramTimestamp) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    return (paramTimestamp == null) ? null : simpleDateFormat.format(paramTimestamp);
  }
  
  private String getColumnValue(ResultSet paramResultSet, int paramInt1, int paramInt2) throws SQLException, IOException {
    boolean bool;
    Clob clob;
    String str = "";
    switch (paramInt1) {
      case -7:
      case 2000:
        str = handleObject(paramResultSet.getObject(paramInt2));
        break;
      case 16:
        bool = paramResultSet.getBoolean(paramInt2);
        str = Boolean.valueOf(bool).toString();
        break;
      case 2005:
      case 2011:
        clob = paramResultSet.getClob(paramInt2);
        if (clob != null)
          str = read(clob); 
        break;
      case -5:
        str = handleLong(paramResultSet, paramInt2);
        break;
      case 2:
      case 3:
      case 6:
      case 7:
      case 8:
        str = handleBigDecimal(paramResultSet.getBigDecimal(paramInt2));
        break;
      case -6:
      case 4:
      case 5:
        str = handleInteger(paramResultSet, paramInt2);
        break;
      case 91:
        str = handleDate(paramResultSet, paramInt2);
        break;
      case 92:
        str = handleTime(paramResultSet.getTime(paramInt2));
        break;
      case 93:
        str = handleTimestamp(paramResultSet.getTimestamp(paramInt2));
        break;
      case -16:
      case -15:
      case -9:
      case -1:
      case 1:
      case 12:
        str = paramResultSet.getString(paramInt2);
        break;
      default:
        str = "";
        break;
    } 
    if (str == null)
      str = ""; 
    return str;
  }
  
  private static String read(Clob paramClob) throws SQLException, IOException {
    StringBuilder stringBuilder = new StringBuilder((int)paramClob.length());
    Reader reader = paramClob.getCharacterStream();
    char[] arrayOfChar = new char[2048];
    int i;
    while ((i = reader.read(arrayOfChar, 0, arrayOfChar.length)) != -1)
      stringBuilder.append(arrayOfChar, 0, i); 
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\ResultSetHelperService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */