package writer;

import java.io.OutputStream;

/**
 * Created by acoustically on 16. 12. 5.
 */
public class StringWriter extends DataWriter {
  private String stringData;

  public StringWriter(OutputStream outputStream, String stringData) throws Exception{
    super(outputStream);
    this.stringData = stringData;
  }

  @Override
  public void write() {
    try {
      writer.write(1);
      writer.write(lengthToBytes(stringToBytes(stringData).length));
      writer.write(stringToBytes(stringData));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
