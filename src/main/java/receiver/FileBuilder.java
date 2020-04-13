package receiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import utils.Packet;

public class FileBuilder {

  private static final Logger LOGGER = Logger.getLogger(FileBuilder.class.getName());

  private static final Map<Integer, Packet> segments = new TreeMap<>();

  private final FileOutputStream fos;

  private final File file;

  private ReceiverRDT rdt;

  public FileBuilder(String fileName, ReceiverRDT receiver) throws FileNotFoundException {
    file = new File(fileName);
    fos = new FileOutputStream(file);
    rdt = receiver;
  }

  public void write(byte[] bytes) {
    try {
      fos.write(bytes);
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException();
    }
  }

  public void close() {
    try {
      fos.close();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException();
    }
  }

  public void buildFile() {
    while (true) {
      Packet rcvpkt = rdt.receive().get();
      if (rcvpkt.FIN) {
        break;
      }
      segments.putIfAbsent(rcvpkt.sequenceNumber, rcvpkt);
    }

  }

}
