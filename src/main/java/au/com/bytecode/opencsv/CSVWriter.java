package au.com.bytecode.opencsv;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CSVWriter implements Closeable {
  public static final int INITIAL_STRING_SIZE = 128;
  
  private Writer rawWriter;
  
  private PrintWriter pw;
  
  private char separator;
  
  private char quotechar;
  
  private char escapechar;
  
  private String lineEnd;
  
  public static final char DEFAULT_ESCAPE_CHARACTER = '"';
  
  public static final char DEFAULT_SEPARATOR = ',';
  
  public static final char DEFAULT_QUOTE_CHARACTER = '"';
  
  public static final char NO_QUOTE_CHARACTER = '\000';
  
  public static final char NO_ESCAPE_CHARACTER = '\000';
  
  public static final String DEFAULT_LINE_END = "\n";
  
  private ResultSetHelper resultService = new ResultSetHelperService();
  
  public CSVWriter(Writer paramWriter) {
    this(paramWriter, ',');
  }
  
  public CSVWriter(Writer paramWriter, char paramChar) {
    this(paramWriter, paramChar, '"');
  }
  
  public CSVWriter(Writer paramWriter, char paramChar1, char paramChar2) {
    this(paramWriter, paramChar1, paramChar2, '"');
  }
  
  public CSVWriter(Writer paramWriter, char paramChar1, char paramChar2, char paramChar3) {
    this(paramWriter, paramChar1, paramChar2, paramChar3, "\n");
  }
  
  public CSVWriter(Writer paramWriter, char paramChar1, char paramChar2, String paramString) {
    this(paramWriter, paramChar1, paramChar2, '"', paramString);
  }
  
  public CSVWriter(Writer paramWriter, char paramChar1, char paramChar2, char paramChar3, String paramString) {
    this.rawWriter = paramWriter;
    this.pw = new PrintWriter(paramWriter);
    this.separator = paramChar1;
    this.quotechar = paramChar2;
    this.escapechar = paramChar3;
    this.lineEnd = paramString;
  }
  
  public void writeAll(List<String[]> paramList) {
    for (String[] arrayOfString : paramList)
      writeNext(arrayOfString); 
  }
  
  protected void writeColumnNames(ResultSet paramResultSet) throws SQLException {
    writeNext(this.resultService.getColumnNames(paramResultSet));
  }
  
  public void writeAll(ResultSet paramResultSet, boolean paramBoolean) throws SQLException, IOException {
    if (paramBoolean)
      writeColumnNames(paramResultSet); 
    while (paramResultSet.next())
      writeNext(this.resultService.getColumnValues(paramResultSet)); 
  }
  
  public void writeNext(String[] paramArrayOfString) {
    if (paramArrayOfString == null)
      return; 
    StringBuilder stringBuilder = new StringBuilder(128);
    for (byte b = 0; b < paramArrayOfString.length; b++) {
      if (b != 0)
        stringBuilder.append(this.separator); 
      String str = paramArrayOfString[b];
      if (str != null) {
        if (this.quotechar != '\000')
          stringBuilder.append(this.quotechar); 
        stringBuilder.append(stringContainsSpecialCharacters(str) ? processLine(str) : str);
        if (this.quotechar != '\000')
          stringBuilder.append(this.quotechar); 
      } 
    } 
    stringBuilder.append(this.lineEnd);
    this.pw.write(stringBuilder.toString());
  }
  
  private boolean stringContainsSpecialCharacters(String paramString) {
    return (paramString.indexOf(this.quotechar) != -1 || paramString.indexOf(this.escapechar) != -1);
  }
  
  protected StringBuilder processLine(String paramString) {
    StringBuilder stringBuilder = new StringBuilder(128);
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      if (this.escapechar != '\000' && c == this.quotechar) {
        stringBuilder.append(this.escapechar).append(c);
      } else if (this.escapechar != '\000' && c == this.escapechar) {
        stringBuilder.append(this.escapechar).append(c);
      } else {
        stringBuilder.append(c);
      } 
    } 
    return stringBuilder;
  }
  
  public void flush() throws IOException {
    this.pw.flush();
  }
  
  public void close() throws IOException {
    flush();
    this.pw.close();
    this.rawWriter.close();
  }
  
  public boolean checkError() {
    return this.pw.checkError();
  }
  
  public void setResultService(ResultSetHelper paramResultSetHelper) {
    this.resultService = paramResultSetHelper;
  }
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\CSVWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */