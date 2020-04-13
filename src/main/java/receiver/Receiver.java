package receiver;

import java.io.IOException;
import utils.UDT;

public class Receiver {

  public static void main(String[] args) throws IOException {

    UDT client = new UDT(8081, "localhost", 8080);

    ReceiverRDT rdt = new ReceiverRDT(client);

    FileBuilder builder = new FileBuilder("asdf.png", rdt);

    builder.buildFile();
    builder.close();

  }
}