package au.com.bytecode.opencsv;

import java.io.IOException;
import java.util.ArrayList;

public class CSVParser {
  private final char separator;
  
  private final char quotechar;
  
  private final char escape;
  
  private final boolean strictQuotes;
  
  private String pending;
  
  private boolean inField = false;
  
  private final boolean ignoreLeadingWhiteSpace;
  
  public static final char DEFAULT_SEPARATOR = ',';
  
  public static final int INITIAL_READ_SIZE = 128;
  
  public static final char DEFAULT_QUOTE_CHARACTER = '"';
  
  public static final char DEFAULT_ESCAPE_CHARACTER = '\\';
  
  public static final boolean DEFAULT_STRICT_QUOTES = false;
  
  public static final boolean DEFAULT_IGNORE_LEADING_WHITESPACE = true;
  
  public static final char NULL_CHARACTER = '\000';
  
  public CSVParser() {
    this(',', '"', '\\');
  }
  
  public CSVParser(char paramChar) {
    this(paramChar, '"', '\\');
  }
  
  public CSVParser(char paramChar1, char paramChar2) {
    this(paramChar1, paramChar2, '\\');
  }
  
  public CSVParser(char paramChar1, char paramChar2, char paramChar3) {
    this(paramChar1, paramChar2, paramChar3, false);
  }
  
  public CSVParser(char paramChar1, char paramChar2, char paramChar3, boolean paramBoolean) {
    this(paramChar1, paramChar2, paramChar3, paramBoolean, true);
  }
  
  public CSVParser(char paramChar1, char paramChar2, char paramChar3, boolean paramBoolean1, boolean paramBoolean2) {
    if (anyCharactersAreTheSame(paramChar1, paramChar2, paramChar3))
      throw new UnsupportedOperationException("The separator, quote, and escape characters must be different!"); 
    if (paramChar1 == '\000')
      throw new UnsupportedOperationException("The separator character must be defined!"); 
    this.separator = paramChar1;
    this.quotechar = paramChar2;
    this.escape = paramChar3;
    this.strictQuotes = paramBoolean1;
    this.ignoreLeadingWhiteSpace = paramBoolean2;
  }
  
  private boolean anyCharactersAreTheSame(char paramChar1, char paramChar2, char paramChar3) {
    return (isSameCharacter(paramChar1, paramChar2) || isSameCharacter(paramChar1, paramChar3) || isSameCharacter(paramChar2, paramChar3));
  }
  
  private boolean isSameCharacter(char paramChar1, char paramChar2) {
    return (paramChar1 != '\000' && paramChar1 == paramChar2);
  }
  
  public boolean isPending() {
    return (this.pending != null);
  }
  
  public String[] parseLineMulti(String paramString) throws IOException {
    return parseLine(paramString, true);
  }
  
  public String[] parseLine(String paramString) throws IOException {
    return parseLine(paramString, false);
  }
  
  private String[] parseLine(String paramString, boolean paramBoolean) throws IOException {
    if (!paramBoolean && this.pending != null)
      this.pending = null; 
    if (paramString == null) {
      if (this.pending != null) {
        String str = this.pending;
        this.pending = null;
        return new String[] { str };
      } 
      return null;
    } 
    ArrayList<String> arrayList = new ArrayList();
    StringBuilder stringBuilder = new StringBuilder(128);
    boolean bool = false;
    if (this.pending != null) {
      stringBuilder.append(this.pending);
      this.pending = null;
      bool = true;
    } 
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      if (c == this.escape) {
        if (isNextCharacterEscapable(paramString, (bool || this.inField), b)) {
          stringBuilder.append(paramString.charAt(b + 1));
          b++;
        } 
      } else if (c == this.quotechar) {
        if (isNextCharacterEscapedQuote(paramString, (bool || this.inField), b)) {
          stringBuilder.append(paramString.charAt(b + 1));
          b++;
        } else {
          if (!this.strictQuotes && b > 2 && paramString.charAt(b - 1) != this.separator && paramString.length() > b + 1 && paramString.charAt(b + 1) != this.separator)
            if (this.ignoreLeadingWhiteSpace && stringBuilder.length() > 0 && isAllWhiteSpace(stringBuilder)) {
              stringBuilder.setLength(0);
            } else {
              stringBuilder.append(c);
            }  
          bool = !bool ? true : false;
        } 
        this.inField = !this.inField;
      } else if (c == this.separator && !bool) {
        arrayList.add(stringBuilder.toString());
        stringBuilder.setLength(0);
        this.inField = false;
      } else if (!this.strictQuotes || bool) {
        stringBuilder.append(c);
        this.inField = true;
      } 
    } 
    if (bool)
      if (paramBoolean) {
        stringBuilder.append("\n");
        this.pending = stringBuilder.toString();
        stringBuilder = null;
      } else {
        throw new IOException("Un-terminated quoted field at end of CSV line");
      }  
    if (stringBuilder != null)
      arrayList.add(stringBuilder.toString()); 
    return arrayList.<String>toArray(new String[arrayList.size()]);
  }
  
  private boolean isNextCharacterEscapedQuote(String paramString, boolean paramBoolean, int paramInt) {
    return (paramBoolean && paramString.length() > paramInt + 1 && paramString.charAt(paramInt + 1) == this.quotechar);
  }
  
  protected boolean isNextCharacterEscapable(String paramString, boolean paramBoolean, int paramInt) {
    return (paramBoolean && paramString.length() > paramInt + 1 && (paramString.charAt(paramInt + 1) == this.quotechar || paramString.charAt(paramInt + 1) == this.escape));
  }
  
  protected boolean isAllWhiteSpace(CharSequence paramCharSequence) {
    boolean bool = true;
    for (byte b = 0; b < paramCharSequence.length(); b++) {
      char c = paramCharSequence.charAt(b);
      if (!Character.isWhitespace(c))
        return false; 
    } 
    return bool;
  }
}


/* Location:              C:\Users\yangs\Downloads\Waila_1.3.10_1.5.2.zip!\au\com\bytecode\opencsv\CSVParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */