import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class TeamPicker {
    public static void main(String args[]){
        ArrayList<String> people = new ArrayList<String>();
        JFrame frame = new JFrame("Team Picker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,700);

        //Build the Panel for team info
        JPanel topPanel = new JPanel();
        JLabel numTeamLabel = new JLabel("Number of Teams");
        JTextField numTeams = new JTextField(3);
        JButton makeTeams = new JButton("Make Teams");
        topPanel.add(numTeamLabel);
        topPanel.add(numTeams);
        topPanel.add(makeTeams);

        //Build the Panel for inputting users
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter Person's Name");
        JTextField participantName = new JTextField(30);
        JButton addUser = new JButton("Add Person");
        panel.add(label);
        panel.add(participantName);
        panel.add(addUser);

        //Make TextArea for center of screen
        JTextPane tp = new JTextPane();
        StyledDocument doc = tp.getStyledDocument();

        addUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!participantName.getText().isEmpty()){
                    try{
                        doc.insertString(doc.getLength(),participantName.getText() + "\n", null);
                    } catch (Exception ex){}
                    people.add(participantName.getText());
                    participantName.setText("");
                }
                else{
                    JOptionPane.showMessageDialog(frame, "Please enter at least one character", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        makeTeams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int amountOfTeams = Integer.parseInt(numTeams.getText());

                    ArrayList<ArrayList<String>> teams = new ArrayList<>();
                    for (int i = 0; i < amountOfTeams; i++){
                        teams.add(new ArrayList<String>());
                    }
                    int count = 0;
                    while(!people.isEmpty()){
                        var randPos = (int)(Math.random() * people.size());
                        teams.get(count).add(people.get(randPos));
                        people.remove(randPos);
                        count = count < amountOfTeams - 1 ? count + 1 : 0;
                    }

                    for(var team : teams){
                        System.out.println("Team");
                        for (var person : team){
                            System.out.println(person);
                        }
                    }
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please Type in a Valid number of teams", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.getContentPane().add(BorderLayout.NORTH, topPanel);
        frame.getContentPane().add(BorderLayout.CENTER, tp);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.setVisible(true);
    }
}
