import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.sun.activation.registries.LogSupport.log;

/**
 * Created by acoustically on 16. 11. 29.
 */
public class ReadWriteThread extends Thread {
  private StreamConnection channel = null;
  private InputStream btIn = null;
  private OutputStream btOut = null;

  public ReadWriteThread(StreamConnection channel) throws IOException {
    this.channel = channel;
    this.btIn = channel.openInputStream();
    this.btOut = channel.openOutputStream();
  }
  public void run() {
    try {
      while (true) {
        System.out.println("ready");
        int data = readData();
        if (data == 10) {
          btOut.write(stringToBytes("some strings"));
          btOut.flush();
        } else if(data == -1) {
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("Exception occur");
      e.printStackTrace();
    } finally {
      close();
    }
  }
  private byte[] stringToBytes(String string) {
    return string.getBytes();
  }
  private int readData() throws Exception{
    int data = btIn.read();
    System.out.println("Receive:" + data);
    System.out.println(data + "");
    return data;
  }
  public void close() {
    log("Session Close");
    if (btIn    != null) try {btIn.close();} catch (Exception e) {/*ignore*/}
    if (btOut   != null) try {btOut.close();} catch (Exception e) {/*ignore*/}
    if (channel != null) try {channel.close();} catch (Exception e) {/*ignore*/}
  }
}
