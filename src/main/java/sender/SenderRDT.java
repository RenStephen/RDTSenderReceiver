package sender;

import java.util.Optional;
import utils.Packet;
import utils.RDT;
import utils.UDT;

public class SenderRDT extends RDT {

  private UDT udt;

  public SenderRDT(UDT udt) {
    this.udt = udt;
  }

  @Override
  public void send(Packet sndpkt) {
    while (true) {
      udt.send(sndpkt.getBytes());
      Optional<Packet> opt = receive();
      if (opt.isPresent()) {
        Packet rcvpkt = opt.get();
        if (!corrupt(rcvpkt) && rcvpkt.ACK && rcvpkt.sequenceNumber == sndpkt.sequenceNumber) {
          return;
        }
      }
    }
  }

  @Override
  public Optional<Packet> receive() {
    byte[] rcvbytes = udt.receive();
    return extract(rcvbytes);
  }
}
