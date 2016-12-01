/**
 * Created by acoustically on 16. 11. 25.
 */

import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;

public class BluetoothServer {
  static final String serverUUID = "0000110100001000800000805f9b34fb";
  private StreamConnectionNotifier server;

  public BluetoothServer() throws IOException {
    server = (StreamConnectionNotifier) Connector.open(
      "btspp://localhost:" + serverUUID,
      Connector.READ_WRITE, true
    );

    ServiceRecord record = LocalDevice.getLocalDevice().getRecord(server);
    LocalDevice.getLocalDevice().updateRecord(record);
  }
  public ReadWriteThread accept() throws IOException {
    System.out.println("Accept");
    StreamConnection channel = server.acceptAndOpen();
    System.out.println("Connected");
    return new ReadWriteThread(channel);
  }
}
