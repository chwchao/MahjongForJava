import java.io.*;
import java.net.*;

import java.awt.*;
import java.swing.*;

import javax.swing.JFrame;

public class Cli{
    public static void main(String[] args){
        try{
            //Creating client and connecting
            Socket cli = new Socket("61.227.246.22", 1233);

            //Setup for window
            JFrame frame = new JFrame();
            frame.setSize(1600, 1024);
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

            c1.close();

        }catch(IOException e){
            System.out.println("Error");
        }
    }
}
