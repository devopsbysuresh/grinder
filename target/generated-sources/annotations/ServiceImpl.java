import flipkart.grinder;
import java.lang.Exception;
import java.lang.Override;
import java.lang.Runtime;
import java.lang.String;
import java.lang.System;
import java.lang.Thread;
import micronet.network.Context;
import micronet.network.IPeer;
import micronet.network.Request;
import micronet.network.factory.PeerFactory;

public final class ServiceImpl {
  public static void main(String[] args) {
    try {
      System.out.println("Starting grinder...");

      IPeer peer = PeerFactory.createPeer();
      Context context = new Context(peer, "mn://player");
      grinder service = new grinder();

      System.out.println("Registering message listeners...");
      peer.listen("/add", (Request request) -> service.addPlayer(context, request));
      peer.listen("/score/add", (Request request) -> service.addScore(context, request));
      peer.listen("/score/broadcast", (Request request) -> service.broadcastScore(context, request));
      peer.listen("/score/all", (Request request) -> service.getScores(context, request));
      peer.listen("/remove", (Request request) -> service.removePlayer(context, request));

      System.out.println("grinder started...");
      service.onStart(context);

      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          System.out.println("grinder stopped...");
          service.onStop(context);
        }
      });
    }
    catch (Exception e) {
      System.err.println("Could not start grinder...");
      e.printStackTrace();
    }
  }
}
