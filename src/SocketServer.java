/**
 * The below utilizes an open source Java library called WebSocket
 * Documentation: https://github.com/TooTallNate/Java-WebSocket/wiki#server-example
 */

import java.io.File;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


public class SocketServer extends WebSocketServer {

    private DigitalDining restaurant;
    private Gson gson = new Gson();

    public SocketServer(InetSocketAddress address, DigitalDining restaurant) {
        super(address);
        this.restaurant = restaurant;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        // Send the restaurant data
        conn.send(gson.toJson(restaurant));

        // This method sends a message to all clients connected
        broadcast( "new connection: " + handshake.getResourceDescriptor() );
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

        int firstIndex = message.indexOf('|');
        int lastIndex = message.lastIndexOf('|');
        String part1 = message.substring(firstIndex + 1, lastIndex);
        String part2 = message.substring(lastIndex + 1);

        if (message.startsWith("tip")) {
            // Action: Tip
            int serverIndex = Integer.parseInt(part1);
            double tip = Double.parseDouble(part2);
            restaurant.tipServer(serverIndex, tip);
        } else if (message.startsWith("order")) {
            // Action: Order food
            int serverIndex = Integer.parseInt(part1);
            int orderNumber = Integer.parseInt(part2);
            restaurant.orderFood(serverIndex, orderNumber);
        } else if (message.startsWith("closeTable")) {
            // Action: Close a table
            int serverIndex = Integer.parseInt(part1);
            String refund = message.substring(lastIndex + 1);
            restaurant.closeTable(serverIndex, refund.equals("true"));
        } else if (message.startsWith("fire")) {
            // Action: Fire an employee
            int serverIndex = Integer.parseInt(part1);
            restaurant.fireEmployee(serverIndex);
        }

        // Send the restaurant data
        conn.send(gson.toJson(restaurant));
        // Update the employee text file
        try {
            restaurant.writeEmployeeFile(new PrintWriter(new File("employees.txt")));
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }

}