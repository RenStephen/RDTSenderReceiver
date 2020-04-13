package utils;

import java.nio.BufferUnderflowException;
import java.util.Optional;
import org.apache.log4j.Logger;

public abstract class RDT {

  private static final Logger LOGGER = Logger.getLogger(RDT.class.getName());

  public abstract void send(Packet sndpkt);

  public abstract Optional<Packet> receive();

  public Packet makePacket(int sequenceNumber, byte[] body) {
    return new Packet(sequenceNumber, false, false, false, body);
  }

  public static boolean corrupt(Packet pkt) {
    return (checksum(pkt.getPseudoPacket()) + pkt.checksum) != 0xFFFF;
  }

  protected static int checksum(byte[] pkt) {

    int length = pkt.length;
    int i = 0;
    int sum = 0;

    while (length > 1) {
      int data = ((pkt[i] << 8) & 0xFF00) + (pkt[i + 1] & 0xFF);
      sum += data;
      if (sum >> 16 > 0) {
        sum = sum & 0xFFFF;
        sum += 1;
      }
      i += 2;
      length -= 2;
    }
    if (length > 0) {
      sum += (pkt[i] << 8) & 0xFF00;
      if (sum >> 16 > 0) {
        sum += 1;
        sum = (sum & 0xFFFF);
      }
    }
    return sum;
  }

  protected static Optional<Packet> extract(byte[] pkt) {
    try {
      return Optional.of(new Packet(pkt));
    } catch (BufferUnderflowException e) {
      LOGGER.info("Problem encountered trying to extract packet");
      return Optional.empty();
    }
  }

  protected static byte[] intToBytes(int i) {
    return new byte[]{
        (byte) ((i >> 24) & 0xff),
        (byte) ((i >> 16) & 0xff),
        (byte) ((i >> 8) & 0xff),
        (byte) ((i >> 0) & 0xff)
    };
  }

}
