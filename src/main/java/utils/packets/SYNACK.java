package utils.packets;

import utils.Packet;

public class SYNACK extends Packet {

  private static final String ACK_PRINT_FORMAT = ""
      + "Sequence Number: %d, Length: %d\n"
      + "Header Length: %d\n"
      + "SYNACK: %b\n"
      + "Check Sum: %s\n";

  public SYNACK(int sequenceNumber) {
    super(sequenceNumber, true, true, false, EMPTY);
  }

  @Override
  public String toString() {
    return String.format(ACK_PRINT_FORMAT,
        sequenceNumber, length,
        HEADER_LENGTH, ACK && SYN,
        Integer.toBinaryString(checksum));
  }
}
