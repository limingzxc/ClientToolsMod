package au.com.bytecode.opencsv;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHelper {
  String[] getColumnNames(ResultSet paramResultSet) throws SQLException;
  
  String[] getColumnValues(ResultSet paramResultSet) throws SQLException, IOException;
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\ResultSetHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */