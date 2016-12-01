import javax.imageio.ImageIO;
import javax.microedition.io.StreamConnection;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

/**
 * Created by acoustically on 16. 11. 29.
 */
public class ReadWriteThread extends Thread {
  private StreamConnection channel;
  private InputStream btIn;
  private OutputStream btOut;
  private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

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
          String stringData = getStringFromClipboard();
          BufferedImage imageData = getImageFromClipboard();
          if(imageData == null) {
            btOut.write(1);
            btOut.write(lengthToBytes(stringToBytes(stringData).length));
            btOut.write(stringToBytes(stringData));
          } else if (stringData == null) {
            btOut.write(2);
            btOut.write(lengthToBytes(Files.readAllBytes(imageToFile(imageData).toPath()).length));
            btOut.write(fileToBytes(imageToFile(imageData)));
          }
          btOut.flush();
        } else if(data == -1) {
          close();
          this.close();
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
  private String getStringFromClipboard() throws Exception {
    Transferable contents = getContentsClipboard();
    if(contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
      return (String) contents.getTransferData(DataFlavor.stringFlavor);
    } else {
      return null;
    }
  }
  private BufferedImage getImageFromClipboard() throws Exception {
    Transferable contents = getContentsClipboard();
    if(contents.isDataFlavorSupported(DataFlavor.imageFlavor)) {
      return (BufferedImage) contents.getTransferData(DataFlavor.imageFlavor);
    } else {
      return null;
    }
  }
  private Transferable getContentsClipboard() throws Exception {
    return clipboard.getContents(clipboard);
  }
  private byte[] stringToBytes(String string) {
    return string.getBytes();
  }
  private byte[] fileToBytes(File file) throws Exception{
    return Files.readAllBytes(file.toPath());
  }
  private byte[] lengthToBytes(int langth) throws Exception{
    System.out.println(langth + "");
    return stringToBytes(langth + "");
  }
  private File imageToFile(BufferedImage image) throws Exception{
    File imageFile = new File("TempImage.png");
    ImageIO.write(image, "PNG", imageFile);
    return imageFile;
  }
  private int readData() throws Exception{
    int data = btIn.read();
    System.out.println("Receive:" + data);
    return data;
  }
  public void close() {
    System.out.println("Session Close");
    try {
      if (btIn != null)
        btIn.close();
      if (btOut != null)
        btOut.close();
      if (channel != null)
        channel.close();
    }
    catch (Exception e)
    {
      System.out.println("Close Exception");
    }
  }
}
