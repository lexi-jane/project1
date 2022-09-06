import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.awt.dnd.*;
import java.util.List;

class MyDragDropListener implements DropTargetListener {
    public JFrame dragNDrop;
    public TeamPicker teamPicker;
    @Override
    public void drop(DropTargetDropEvent event) {
        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors) {

            try {

                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {

                    // Get all of the dropped files
                    List files = (List) transferable.getTransferData(flavor);

                    // Loop them through
                    for (var file : files) {
                        var data = new String(Files.readAllBytes(Paths.get(((File) file).getPath())));
                        var people = new ArrayList<String>();
                        for(var person : data.split(",")){
                            people.add(person);
                        }
                        teamPicker.setPeople(people);
                        teamPicker.updateTextArea(people);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dragNDrop.setVisible(false);
        dragNDrop.dispose();
        // Inform that the drop is complete
        event.dropComplete(true);
    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
    }

    @Override
    public void dragExit(DropTargetEvent event) {
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
    }

}

class TeamPicker {
    private JFrame dragNDrop;
    private List<String> people;
    private StyledDocument doc;

    public void openDragNDrop(){
        dragNDrop = new JFrame("Upload File");
        dragNDrop.setSize(500,300);

        JLabel myLabel = new JLabel("Drag file here", SwingConstants.CENTER);

        // Create the drag and drop listener
        MyDragDropListener myDragDropListener = new MyDragDropListener();
        myDragDropListener.dragNDrop = dragNDrop;
        myDragDropListener.teamPicker = this;

        // Connect the label with a drag and drop listener
        new DropTarget(myLabel, myDragDropListener);

        // Add the label to the content
        dragNDrop.getContentPane().add(BorderLayout.CENTER, myLabel);

        // Show the frame
        dragNDrop.setVisible(true);
    }

    public void openTeamPicker(){
        people = new ArrayList<String>();
        JFrame frame = new JFrame("Team Picker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,700);

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
        JButton uploadUserFile = new JButton("Upload User File");
        panel.add(label);
        panel.add(participantName);
        panel.add(addUser);
        panel.add(uploadUserFile);

        //Make TextArea for center of screen
        JTextPane tp = new JTextPane();
        JScrollPane sp = new JScrollPane(tp);
        doc = tp.getStyledDocument();

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
                if(people.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Can't make teams without users!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        int amountOfTeams = Integer.parseInt(numTeams.getText());

                        ArrayList<ArrayList<String>> teams = new ArrayList<>();
                        for (int i = 0; i < amountOfTeams; i++) {
                            teams.add(new ArrayList<String>());
                        }
                        int count = 0;
                        while (!people.isEmpty()) {
                            var randPos = (int) (Math.random() * people.size());
                            teams.get(count).add(people.get(randPos));
                            people.remove(randPos);
                            count = count < amountOfTeams - 1 ? count + 1 : 0;
                        }

                        for (var team : teams) {
                            System.out.println("Team");
                            for (var person : team) {
                                System.out.println(person);
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Please Type in a Valid number of teams", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        uploadUserFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDragNDrop();
            }
        });

        frame.getContentPane().add(BorderLayout.NORTH, topPanel);
        frame.getContentPane().add(BorderLayout.CENTER, sp);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.setVisible(true);
    }

    public void setPeople(ArrayList<String> peopleList){
        people = peopleList;
    }

    public void updateTextArea(ArrayList<String> people){
        try{
            doc.remove(0, doc.getLength());
        } catch(Exception ex){}

        for(var person : people){
            try {
                doc.insertString(doc.getLength(), person + "\n", null);
            } catch(Exception ex){}
        }
    }
}

public class Main {
    public static void main(String args[]){
        var teamPicker = new TeamPicker();

        teamPicker.openTeamPicker();
    }
}
