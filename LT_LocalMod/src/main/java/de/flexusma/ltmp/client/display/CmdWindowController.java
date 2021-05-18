package de.flexusma.ltmp.client.display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import com.lilithsthrone.main.Main;
import de.flexusma.ltmp.client.Setup;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CmdWindowController {

    @FXML
    private TextArea console;
    private PrintStream ps ;
    Button connect;


    public void initialize() {

        BorderPane pane = new BorderPane();
        console = new TextArea();
        console.setEditable(false);
        pane.setCenter(console);
        connect = new Button("Connect");
        connect.setOnAction(this::button);
        pane.setBottom(connect);


        Scene scene = new Scene(pane,600,400);

        Stage stage = new Stage();
            stage.setTitle("Log");
            stage.setScene(scene);
            stage.show();

        ps = new PrintStream(new FConsole(console)) ;
    }

    public void button(ActionEvent event) {

        if(Main.isConnected){
            try {
                Setup.socketClient.getKclient().dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       // System.setOut(ps);
       // System.setErr(ps);
        System.out.println("Testing server connection + registering");
        Setup.main(null);
        connect.setText("RE-Connect (Will break current interactions!)");

    }

    public class FConsole extends OutputStream {
        private TextArea console;

        public FConsole(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }


}
