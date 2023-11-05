package au.com.bytecode.opencsv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader implements Closeable {
  private BufferedReader br;
  
  private boolean hasNext = true;
  
  private CSVParser parser;
  
  private int skipLines;
  
  private boolean linesSkiped;
  
  public static final int DEFAULT_SKIP_LINES = 0;
  
  public CSVReader(Reader paramReader) {
    this(paramReader, ',', '"', '\\');
  }
  
  public CSVReader(Reader paramReader, char paramChar) {
    this(paramReader, paramChar, '"', '\\');
  }
  
  public CSVReader(Reader paramReader, char paramChar1, char paramChar2) {
    this(paramReader, paramChar1, paramChar2, '\\', 0, false);
  }
  
  public CSVReader(Reader paramReader, char paramChar1, char paramChar2, boolean paramBoolean) {
    this(paramReader, paramChar1, paramChar2, '\\', 0, paramBoolean);
  }
  
  public CSVReader(Reader paramReader, char paramChar1, char paramChar2, char paramChar3) {
    this(paramReader, paramChar1, paramChar2, paramChar3, 0, false);
  }
  
  public CSVReader(Reader paramReader, char paramChar1, char paramChar2, int paramInt) {
    this(paramReader, paramChar1, paramChar2, '\\', paramInt, false);
  }
  
  public CSVReader(Reader paramReader, char paramChar1, char paramChar2, char paramChar3, int paramInt) {
    this(paramReader, paramChar1, paramChar2, paramChar3, paramInt, false);
  }
  
  public CSVReader(Reader paramReader, char paramChar1, char paramChar2, char paramChar3, int paramInt, boolean paramBoolean) {
    this(paramReader, paramChar1, paramChar2, paramChar3, paramInt, paramBoolean, true);
  }
  
  public CSVReader(Reader paramReader, char paramChar1, char paramChar2, char paramChar3, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    this.br = new BufferedReader(paramReader);
    this.parser = new CSVParser(paramChar1, paramChar2, paramChar3, paramBoolean1, paramBoolean2);
    this.skipLines = paramInt;
  }
  
  public List<String[]> readAll() throws IOException {
    ArrayList<String[]> arrayList = new ArrayList();
    while (this.hasNext) {
      String[] arrayOfString = readNext();
      if (arrayOfString != null)
        arrayList.add(arrayOfString); 
    } 
    return (List<String[]>)arrayList;
  }
  
  public String[] readNext() throws IOException {
    String[] arrayOfString = null;
    while (true) {
      String str = getNextLine();
      if (!this.hasNext)
        return arrayOfString; 
      String[] arrayOfString1 = this.parser.parseLineMulti(str);
      if (arrayOfString1.length > 0)
        if (arrayOfString == null) {
          arrayOfString = arrayOfString1;
        } else {
          String[] arrayOfString2 = new String[arrayOfString.length + arrayOfString1.length];
          System.arraycopy(arrayOfString, 0, arrayOfString2, 0, arrayOfString.length);
          System.arraycopy(arrayOfString1, 0, arrayOfString2, arrayOfString.length, arrayOfString1.length);
          arrayOfString = arrayOfString2;
        }  
      if (!this.parser.isPending())
        return arrayOfString; 
    } 
  }
  
  private String getNextLine() throws IOException {
    if (!this.linesSkiped) {
      for (byte b = 0; b < this.skipLines; b++)
        this.br.readLine(); 
      this.linesSkiped = true;
    } 
    String str = this.br.readLine();
    if (str == null)
      this.hasNext = false; 
    return this.hasNext ? str : null;
  }
  
  public void close() throws IOException {
    this.br.close();
  }
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\CSVReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */