import java.io.*;
import java.net.*;

import java.awt.*;
import javax.swing.*;

import javax.swing.JFrame;

public class Cli{
    public static void main(String[] args){
        try{
            //Creating client and connecting
            Socket cli = new Socket("36.239.248.103", 1233);

            //Setup for window
            JFrame frame = new JFrame();
            frame.setSize(1024, 768);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            //Reader
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            //Writer
            DataOutputStream out = new DataOutputStream(cli.getOutputStream());

            String tmp;

            //Action of reading and writing through server
            tmp = in.readLine();
            out.writeUTF(tmp);

            cli.close();

        }catch(IOException e){
            System.out.println("Error");
        }
    }
}
