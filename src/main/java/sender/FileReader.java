package sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.apache.log4j.Logger;
import utils.Packet;

public class FileReader {

  private static final int STREAM_BUFFER = 1000;

  private static final Logger LOGGER = Logger.getLogger(FileReader.class.getName());

  private static final Map<Integer, Packet> packets = new LinkedHashMap<>();

  private SenderRDT rdt;

  private FileInputStream stream;

  private int sequenceNumber = 0;

  public FileReader(String path, SenderRDT sender) {
    try {
      File file = new File(path);
      stream = new FileInputStream(file);
      rdt = sender;

      Optional<byte[]> b = getFileContent();
      while (b.isPresent()) {
        Packet pkt = rdt.makePacket(sequenceNumber, b.get());
        packets.putIfAbsent(sequenceNumber, pkt);
        sequenceNumber++;
        b = getFileContent();
      }


    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException();
    }
  }

  private Optional<byte[]> getFileContent() throws IOException {
    int available = stream.available();
    int b = Math.min(available, STREAM_BUFFER);
    byte[] arr = new byte[b];

    int data = stream.read(arr);

    if (data == 0 || data == -1) {
      stream.close();
      LOGGER.info("EOF reached");
      return Optional.empty();
    }
    return Optional.of(arr);
  }

  public int sendFile() {
    for (Entry<Integer, Packet> entry : packets.entrySet()) {
      Packet sndpkt = entry.getValue();
      rdt.send(sndpkt);
    }
    return sequenceNumber;
  }

}
