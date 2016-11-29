/**
 * Created by acoustically on 16. 11. 25.
 */

import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class BluetoothServer {
  //Standard SerialPortService ID
  static final String serverUUID = "0000110100001000800000805f9b34fb";
  //static final String serverUUID = "11111111111111111111111111111123";

  private StreamConnectionNotifier server = null;

  public BluetoothServer() throws IOException {
    server = (StreamConnectionNotifier) Connector.open(
      "btspp://localhost:" + serverUUID,
      Connector.READ_WRITE, true
    );

    ServiceRecord record = LocalDevice.getLocalDevice().getRecord(server);
    LocalDevice.getLocalDevice().updateRecord(record);
  }
  public Session accept() throws IOException {
    log("Accept");
    StreamConnection channel = server.acceptAndOpen();
    log("Connected");
    return new Session(channel);
  }
  public void dispose() {
    log("Dispose");
    if (server  != null) try {server.close();} catch (Exception e) {/*ignore*/}
  }

  static class Session implements Runnable {
    private StreamConnection channel = null;
    private InputStream btIn = null;
    private OutputStream btOut = null;

    public Session(StreamConnection channel) throws IOException {
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
  public static void main(String[] args) throws Exception {

    BluetoothServer server = new BluetoothServer();
      Session session = server.accept();
      new Thread(session).start();
   }
  private static void log(String msg) {
    System.out.println("["+(new Date()) + "] " + msg);
  }
}
