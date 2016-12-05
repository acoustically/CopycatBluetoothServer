import java.io.File;

/**
 * Created by acoustically on 16. 11. 29.
 */
public class Main {
  public static void main(String[] args) {
    try {
        BluetoothServer server = new BluetoothServer();
        ReadWriteThread readWriteThread = server.accept();
        readWriteThread.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
