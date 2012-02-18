package org.lh.dmlj.schema.editor.template;

public class SegmentTemplate
{
  protected static String nl;
  public static synchronized SegmentTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    SegmentTemplate result = new SegmentTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NL + "" + NL + "<beans xmlns=\"http://www.springframework.org/schema/beans\"" + NL + "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + NL + "       xmlns:dmlj=\"http://www.dmlj.org/schema/segments\"" + NL + "       xsi:schemaLocation=\"" + NL + "     http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd" + NL + "     http://www.dmlj.org/schema/segments http://www.dmlj.org/schema/segments.xsd\">" + NL + "" + NL + "  <dmlj:segment id=\"";
  protected final String TEXT_2 = "\"" + NL + "                maximum-records-per-page=\"";
  protected final String TEXT_3 = "\">" + NL + "    <dmlj:file name=\"";
  protected final String TEXT_4 = "\"" + NL + "               dsname=\"";
  protected final String TEXT_5 = "\"" + NL + "               blocksize=\"";
  protected final String TEXT_6 = "\"/>" + NL + "    <dmlj:area name=\"";
  protected final String TEXT_7 = "\"" + NL + "               pagesize=\"";
  protected final String TEXT_8 = "\"" + NL + "               start-page=\"";
  protected final String TEXT_9 = "\"" + NL + "               end-page=\"";
  protected final String TEXT_10 = "\">" + NL + "      <dmlj:area-to-file-mapping file=\"";
  protected final String TEXT_11 = "\"" + NL + "                                 start-block=\"1\"" + NL + "                                 blocks=\"";
  protected final String TEXT_12 = "\"/>" + NL + "    </dmlj:area>" + NL + "  </dmlj:segment>" + NL + "" + NL + "</beans>";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
Object[] args = (Object[])argument;
String id = (String)args[0];
String areaName = (String)args[1];
int maximumRecordsPerPage = ((Integer)args[2]).intValue();
String dsname = (String)args[3];
int pagesize = ((Integer)args[4]).intValue();
int lowPageNumber = ((Integer)args[5]).intValue();
int highPageNumber = ((Integer)args[6]).intValue();
int pageCount = highPageNumber - lowPageNumber + 1;

    stringBuffer.append(TEXT_1);
    stringBuffer.append( id );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( maximumRecordsPerPage );
    stringBuffer.append(TEXT_3);
    stringBuffer.append( areaName );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( dsname );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( pagesize );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( areaName );
    stringBuffer.append(TEXT_7);
    stringBuffer.append( pagesize );
    stringBuffer.append(TEXT_8);
    stringBuffer.append( lowPageNumber );
    stringBuffer.append(TEXT_9);
    stringBuffer.append( highPageNumber );
    stringBuffer.append(TEXT_10);
    stringBuffer.append( areaName );
    stringBuffer.append(TEXT_11);
    stringBuffer.append( pageCount );
    stringBuffer.append(TEXT_12);
    return stringBuffer.toString();
  }
}
