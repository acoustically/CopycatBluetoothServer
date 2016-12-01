/**
 * Created by acoustically on 16. 11. 29.
 */
public class Main {
  public static void main(String[] args) {
    try {
      while(true) {
        BluetoothServer server = new BluetoothServer();
        ReadWriteThread rwTread = server.accept();
        rwTread.start();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
