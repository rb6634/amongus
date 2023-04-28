/*
 * @Instructor: Alan Mutka
 * @author: Rinor Bugujevci, Uejs Hamja
 * Project: Among us
 * @version: 28/04/2023
 * ISTE: 121.801
 */

import javafx.application.Application;
import javafx.css.CssMetaData;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.Alert.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

//copied from LAB6
public class serverGame extends Application implements EventHandler<ActionEvent> {

    // attributes
    private static final int SERVER_PORT = 2000;
    private Button btnStart = null;
    private boolean isStarted = false;
    private TextArea taLog = null;
    private int clientNo = 0;
    private ArrayList<ObjectOutputStream> outputstream = new ArrayList<>();
    private ArrayList<playerLocation> playerLocation = new ArrayList<>();
    private TextField tfMsg;
    private Button btnSend;

    public static void main(String[] args) {
        // method inside the Application class, it will setup our program as a JavaFX
        // application
        // then the JavaFX is ready, the "start" method will be called automatically
        launch(args);
    }

    @Override
    public void start(Stage _stage) throws Exception {

        /////////////////// Setting window properties
        // set the window title
        _stage.setTitle("Remote File Server (AmongUs)");

        // HBox root layout with 8 pixels spacing
        VBox root = new VBox(8);
        FlowPane fpane = new FlowPane();
        btnStart = new Button("Start");
        fpane.getChildren().addAll(btnStart);
        fpane.setAlignment(Pos.CENTER_RIGHT);
        // create a scene with a specific size (width, height), connnect with the layout
        Scene scene = new Scene(root, 600, 300);
        taLog = new TextArea();
        // root.getChildren().addAll(fpane, taLog);
        btnStart.setOnAction(this);
        tfMsg = new TextField();
        btnSend = new Button("Send");
        HBox chatBox = new HBox(8, tfMsg, btnSend);
        root.getChildren().addAll(fpane, taLog, chatBox);
        btnSend.setOnAction(this);
        // connect stage with the Scene and show it, finalization
        _stage.setX(800);
        _stage.setY(100);
        _stage.setScene(scene);
        _stage.show();

    }

    @Override
    public void handle(ActionEvent event) {
        // new MyServer().start();
        // to do with switch

        Button button = (Button) event.getSource();

        switch (button.getText()) {
            case "Start":

                new MyServer().start();
                // change the text from the button Start to stop
                btnStart.setText("Stop");
                break;
            // change the text from stop button to start
            case "Stop":
                btnStart.setText("Start");
                isStarted = false;
                System.out.println("Server stoped");
                break;
        }
        if (event.getSource() == btnSend) {
            String messageContent = tfMsg.getText().trim();
            if (!messageContent.isEmpty()) {
                ChatMessage message = new ChatMessage("Server", messageContent);
                sendToAllClients(message, -1);
                taLog.appendText("Server" + messageContent + "\n");

                tfMsg.clear();

            }
        }
    }

    Object obj;

    public void sendToAllClients(ChatMessage message, int index) {
        for (int i = 0; i < outputstream.size(); i++) {
            if (i != index) {
                try {
                    outputstream.get(i).writeObject(message);
                    outputstream.get(i).flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    // server class
    class MyServer extends Thread {
        private ServerSocket sSocket = null;
        private Socket cSocket = null;
        String client;
        Object oos;

        // run method
        ObjectInputStream ois;

        public void run() {
            try {
                if (isStarted == false) {
                    sSocket = new ServerSocket(SERVER_PORT);
                }
                isStarted = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (isStarted) {
                // if the the server is working it will showe this println
                System.out.println("Waiting for client to connect");
                try {
                    cSocket = sSocket.accept();
                    MyClient mc = new MyClient(cSocket, clientNo);
                    mc.start();
                    clientNo++;

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof playerLocation) {
                        // empty
                    } else if (obj instanceof ChatMessage) {
                        ChatMessage message = (ChatMessage) obj;

                        log(message.getSender() + ": " + message.getMessage());
                        sendToAllClients(message, -1);
                    } else if (obj instanceof String) {

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }

        private void log(String string) {

        }

    }

    // client class
    class MyClient extends Thread {
        private Socket socket;
        private String currentDir = ".";
        private String client = "C" + clientNo;
        private int index;
        private ObjectInputStream ois = null;
        private ObjectOutputStream oos = null;

        public MyClient(Socket socket, int index) {
            this.socket = socket;
            this.index = index;

        }

        public void run() {
            System.out.println(client + " connected");
            try {
                oos = new ObjectOutputStream(this.socket.getOutputStream());
                ois = new ObjectInputStream(this.socket.getInputStream());
                outputstream.add(oos);
                playerLocation.add(new playerLocation(100, 50, client, this.index));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof playerLocation) {
                        playerLocation location = (playerLocation) obj;
                        playerLocation.get(this.index).setX((int) location.getX());

                        playerLocation.get(this.index).setY((int) location.getY());
                        // System.out.println(client + " " + "x" + location.getX() + "+" + "y" +
                        // location.getY());
                        this.sendToOtherClients(oos, obj);
                    } else if (obj instanceof String) {
                        System.out.println(obj);
                        log((String) obj);

                        // send to all other clients
                        this.sendToOtherClients(oos, obj);

                    } else if (obj instanceof ChatMessage) {
                        ChatMessage message = (ChatMessage) obj;
                        log(message.getSender() + " : " + message.getMessage());
                        sendToAllClients(message, index);
                    }
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                    outputstream.remove(oos);
                    playerLocation.remove(index);
                    break;
                }
            }
        }

        private void sendToOtherClients(ObjectOutputStream oos, Object obj) throws IOException {
            for (ObjectOutputStream objectOutputStream : outputstream) {
                if (objectOutputStream != oos) {
                    System.out.println("Sent!");
                    for (int index = 0; index < playerLocation.size(); index++) {
                        System.out.println(playerLocation.get(index).getIndex());

                    }
                    oos.writeObject(obj);
                    oos.flush();
                    oos.reset();
                }
            }
        }

        public void sendToAllLocation() {

            // i need to send the index to the client
            // when i recreivit i need to update the amongus player
            // who move for that moment to move to the screen for everyone

        }

        // log method to display in the gui
        public void log(String string) {
            taLog.appendText(string);
            taLog.appendText("\n");

        }
    }

}
