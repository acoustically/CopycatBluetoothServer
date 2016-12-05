import writer.DataWriter;
import writer.ImageWriter;
import writer.StringWriter;

import javax.microedition.io.StreamConnection;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by acoustically on 16. 11. 29.
 */
public class ReadWriteThread extends Thread {
  private StreamConnection socket;
  private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  private InputStream reader = socket.openInputStream();
  private OutputStream writer = socket.openOutputStream();
  private DataWriter dataWriter;
  public ReadWriteThread(StreamConnection socket) throws IOException {
    this.socket = socket;
  }
  public void run() {
    try {
      while (true) {
        System.out.println("ready");
        int data = readData();
        if (data == 10) {
          String stringData = getStringFromClipboard();
          BufferedImage imageData = getImageFromClipboard();
          dataWriter = null;
          if(imageData == null) {
            dataWriter = new StringWriter(writer, stringData);
          } else if (stringData == null) {
            dataWriter = new ImageWriter(writer, imageData);
          }
          dataWriter.write();
          writer.flush();
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
  private int readData() throws Exception{
    int data = reader.read();
    System.out.println("Receive:" + data);
    return data;
  }
  public void close() {
    System.out.println("Session Close");
    try {
      if (reader != null)
        reader.close();
      if (writer != null)
        writer.close();
      if (socket != null)
        socket.close();
    }
    catch (Exception e)
    {
      System.out.println("Close Exception");
    }
  }
}
