import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class WindowInterface extends JFrame implements ActionListener{

    JButton Exit_Button, Execute;

    public void MainWindow(){
        setSize(300,200);
        setTitle("MPK Crawler");
        JButton Execute = new JButton("podaj datę");
        Execute.setBounds(100,50,100,20);
        add(Execute);
        Execute.addActionListener(this);

        JButton Exit_Button = new JButton("Wyjście");
        Exit_Button.setBounds(200,50,100,200);
        add(Exit_Button);
        Exit_Button.addActionListener(this);
    }
    public static void okno(String[] args){
        WindowInterface window = new WindowInterface();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

    }
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source==Exit_Button){
            dispose();
        }
        else if (source ==Execute){

        }

    }
}