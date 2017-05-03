import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WindowInterface extends JFrame implements ActionListener{

    public JButton Exit_Button, Execute_Button, PathChoose_Button;
    public JLabel Status_Label, Link_Label, RadioButton_Label;
    public JTextField Link_TextField, Path_TextField;
    public JRadioButton Cracow_RadioButton, Warsaw_RadioButton;
    public ButtonGroup RadioPanel;

    public WindowInterface() throws IOException {

        //Frame initialization
        setSize(850,250);
        setTitle("MPK Crawler");
        setLayout(null);
        setResizable(false);


        //Buttons
        //Implementation of Execute_Button
        Execute_Button = new JButton("Wykonaj");
        Execute_Button.setBounds(600,200,150,20);
        add(Execute_Button);
        Execute_Button.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent aActionEvent ) {
                if ( aActionEvent.getSource() == Execute_Button ){
                    Status_Label.setText("Przetwarzam...");
                    String html = Link_TextField.getText();
                    MPKList mpkList = null;
                    try {
                        mpkList = new MPKList(html);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.print(mpkList);
                    String zmienna = Path_TextField.getText();
                    //
                    try {
                        File file = new File(zmienna+"/output.txt");
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(String.valueOf(mpkList));
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //
                    Status_Label.setText("Gotowe!");
                }
            }
        } );

/*        //Implementation of Exit_Button
        Exit_Button = new JButton("Wyjście");
        Exit_Button.setBounds(50,150,150,20);
        add(Exit_Button);
        Exit_Button.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent aActionEvent ) {
                if ( aActionEvent.getSource() == Exit_Button ){
                    dispose();
                }
            }
        } );*/

        //Implementation of PathChoose_Button
        PathChoose_Button = new JButton("Wybierz ścieżkę zapisu");
        PathChoose_Button.setBounds(10,100,200,20);
        add(PathChoose_Button);
        PathChoose_Button.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent aActionEvent ) {
                if ( aActionEvent.getSource() == PathChoose_Button ){
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new java.io.File("."));
                    chooser.setDialogTitle("Wybierz ścieżkę");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setAcceptAllFileFilterUsed(false);

                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
                        System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
                        Path_TextField.setText(chooser.getSelectedFile().getAbsolutePath());

                    } else {
                        System.out.println("No Selection ");
                    }
                }
            }
        } );


        //Textfields
        //Implementation of Link_TextField
        Link_TextField = new JTextField("");
        Link_TextField.setBounds(10,50, 800, 20);
        add(Link_TextField);
        Link_TextField.addActionListener(this);
        Link_TextField.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent aActionEvent ) {
                if ( aActionEvent.getSource() == Link_TextField){
                    String html = Link_TextField.getText();
                    MPKList mpkList = null;
                    try {
                        mpkList = new MPKList(html);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.print(mpkList);
                }
            }
        } );

        //Implementation of Path_TextField
        Path_TextField = new JTextField("");
        Path_TextField.setBounds(10,150,800,20);
        add(Path_TextField);
        Path_TextField.addActionListener(this);


        //Labels
        //Implementation of Link_Label
        JLabel Link_Label = new JLabel("Wprowadź odpowiedni link:");
        Link_Label.setBounds(10,20,200,20);
        add(Link_Label);

        //Implementation of RadioButton_Label
        JLabel RadioButton_Label = new JLabel("Wybierz miasto:");
        RadioButton_Label.setBounds(380,20,200,20);
        add(RadioButton_Label);

        //Implementation of Status_Label
        Status_Label = new JLabel("Status:");
        Status_Label.setBounds(150,150,150,20);
        add(Status_Label);


        //Radio Buttons
        //Implementation of Cracow_RadioButton
        RadioPanel = new ButtonGroup();
        Cracow_RadioButton = new JRadioButton("Kraków");
        Cracow_RadioButton.setBounds(500,20,80,20);
        RadioPanel.add(Cracow_RadioButton);
        Cracow_RadioButton.setSelected(true);
        add(Cracow_RadioButton);

        //Implementation of Warsaw_RadioButton
        Warsaw_RadioButton = new JRadioButton("Warszawa");
        Warsaw_RadioButton.setBounds(580,20,100,20);
        RadioPanel.add(Warsaw_RadioButton);
        add(Warsaw_RadioButton);

    }
    public static void OpenWindow() throws IOException {
        WindowInterface window = new WindowInterface();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

    }
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source==Exit_Button){
            dispose();
        }
        else if (source==Execute_Button){
            System.out.println("Przetwarzam...");
       }

    }
}