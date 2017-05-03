import javax.swing.JFrame;

/**
 * Created by MIachaI on 03.05.17.
 */

public class WindowInterface extends JFrame{

    public WindowInterface(){
        setSize(300,200);
        setTitle("MPK Crawler");
    }
    public static void okno(String[] args){
        WindowInterface window = new WindowInterface();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

    }
}