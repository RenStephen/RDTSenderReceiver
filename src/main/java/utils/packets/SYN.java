package utils.packets;

import utils.Packet;

public class SYN extends Packet {

  private static final String ACK_PRINT_FORMAT = ""
      + "Sequence Number: %d, Length: %d\n"
      + "Header Length: %d\n"
      + "SYN: %b\n"
      + "Check Sum: %s\n";

  public SYN(int sequenceNumber) {
    super(sequenceNumber, false, true, false, EMPTY);
  }

  @Override
  public String toString() {
    return String.format(ACK_PRINT_FORMAT,
        sequenceNumber, length,
        HEADER_LENGTH, SYN,
        Integer.toBinaryString(checksum));
  }
}
