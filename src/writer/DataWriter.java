package writer;

import java.io.OutputStream;

/**
 * Created by acoustically on 16. 12. 5.
 */
public abstract class DataWriter {
  protected OutputStream writer;

  public DataWriter(OutputStream outputStream) throws Exception{
    writer = outputStream;
  }

  public abstract void write();
  protected byte[] lengthToBytes(int langth) throws Exception{
    System.out.println(langth + "");
    return stringToBytes(langth + "");
  }
  protected byte[] stringToBytes(String string) {
    return string.getBytes();
  }
  public void flush() throws Exception{
    writer.flush();
  }
  public void close() throws Exception {
    writer.close();
  }
}
