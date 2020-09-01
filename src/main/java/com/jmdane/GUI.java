package com.jmdane;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.jmdane.Decompile.*;

class GUI {

    static File filepath;
    static ArrayList<String> JarPaths = new ArrayList<String>();
    static ArrayList<String> Methods = new ArrayList<String>();
    public static JTextArea LoadedArea;
    public static JTextField folderpath;
    public static JTextArea textarea;
    private static JTextField logincode;

    public static void Create() {

        //Creating the Frame
        JFrame frame = new JFrame("Jar Scanner");
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setMaximumSize(new Dimension(900, 700));
        frame.setMinimumSize(new Dimension(900, 700));
        frame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

        //GUI Components

        //Folder path text
        JLabel folderpathtext = new JLabel("Folder Path:");
        folderpathtext.setBounds(12, 637, 70, 10);
        frame.add(folderpathtext);
        //Folder path text area
        folderpath = new JTextField(70); // accepts up to 70 characters
        folderpath.setBounds(90, 631, 620, 23);
        folderpath.setEditable(false);
        frame.add(folderpath);
        //Open File Dialog button
        JButton openfiledialog = new JButton("Open");
        openfiledialog.setBounds(720, 629, 70, 25);
        frame.add(openfiledialog);
        openfiledialog.addActionListener(new ActionListener() {

            JFileChooser chooser;
            String choosertitle;

            public void actionPerformed(ActionEvent e) {
                String userhome = System.getProperty("user.home");
                chooser = new JFileChooser(userhome+"\\Appdata");
                chooser.setDialogTitle(choosertitle);
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                //
                // disable the "All files" option.
                //
                chooser.setAcceptAllFileFilterUsed(false);
                //
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    LoadedArea.setText("");
                    filepath = chooser.getSelectedFile();
                    System.out.println("Get selected folder/file : "
                            +  chooser.getSelectedFile());
                    folderpath.setText(filepath.toString());
                    if(chooser.getSelectedFile().isDirectory()) {
                        JarPaths.clear();
                        for (final File fileEntry : filepath.listFiles()) {
                            if(fileEntry.getName().toString().endsWith(".jar")) {
                                LoadedArea.setText(LoadedArea.getText()+fileEntry.getName().toString()+"\n");
                                JarPaths.add(filepath+"\\"+fileEntry.getName());
                                //System.out.println(filepath+"\\"+fileEntry.getName());
                            }

                        }
                    }
                    if(chooser.getSelectedFile().isFile()) {
                        JarPaths.clear();
                        if(chooser.getSelectedFile().toString().endsWith(".jar")) {
                            //System.out.println(chooser.getSelectedFile().toString());
                            JarPaths.add(chooser.getSelectedFile().toString());
                            String[] jarnamestring = chooser.getSelectedFile().toString().split("\\\\");
                            LoadedArea.setText(jarnamestring[jarnamestring.length - 1]);
                        }
                    }
                }
                else {
                    System.out.println("No Selection ");
                }
            }


        });

        //Scan button
        JButton scan = new JButton("Scan");
        scan.setBounds(800, 629, 70, 25);
        scan.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    DecompiledData.clear();
                    textarea.setText("");
                    Scan.Scan(Methods, JarPaths);
                } catch (IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }


        });
        frame.add(scan);
        // Text Area at the Center
        textarea = new JTextArea();
        textarea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        textarea.setEditable(false);
        textarea.setDropTarget(new DropTarget() {
            public void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List<File>) evt
                            .getTransferable().getTransferData(
                                    DataFlavor.javaFileListFlavor);
                    JarPaths.clear();
                    LoadedArea.setText("");
                    for (File file : droppedFiles) {
                        if(file.toString().endsWith(".jar")) {
                            //System.out.println(chooser.getSelectedFile().toString());
                            JarPaths.add(file.toString());
                            String[] jarnamestring = file.toString().split("\\\\");
                            LoadedArea.setText(LoadedArea.getText()+jarnamestring[jarnamestring.length - 1]+"\n");
                            folderpath.setText(file.toString());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JScrollPane textareascroll = new JScrollPane (textarea);
        textareascroll.setBounds(10, 40, 670, 580);
        frame.add(textareascroll);
        //Loaded jar text
        JLabel loadedjartext = new JLabel("Loaded Jars(below):");
        loadedjartext.setBounds(690, 37, 120, 20);
        frame.add(loadedjartext);
        // Loaded jars area
        LoadedArea = new JTextArea();
        LoadedArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        LoadedArea.setEditable(false);
        LoadedArea.setFont(LoadedArea.getFont().deriveFont(Font.BOLD));
        LoadedArea.setDropTarget(new DropTarget() {
            public void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List<File>) evt
                            .getTransferable().getTransferData(
                                    DataFlavor.javaFileListFlavor);
                    JarPaths.clear();
                    LoadedArea.setText("");
                    for (File file : droppedFiles) {
                        if(file.toString().endsWith(".jar")) {
                            //System.out.println(chooser.getSelectedFile().toString());
                            JarPaths.add(file.toString());
                            String[] jarnamestring = file.toString().split("\\\\");
                            LoadedArea.setText(LoadedArea.getText()+jarnamestring[jarnamestring.length - 1]+"\n");
                            folderpath.setText(file.toString());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JScrollPane LoadedAreaScroll = new JScrollPane (LoadedArea);
        LoadedAreaScroll.setBounds(690, 60, 182, 560);
        frame.add(LoadedAreaScroll);
        //Search bar input
        final JPasswordField searchbar = new JPasswordField(70); // accepts up to 70 characters
        searchbar.setBounds(10, 10, 670, 25);
        frame.add(searchbar);
        //Hide strings checkbox
        JCheckBox hidestringcheck = new JCheckBox("Hide Strings");
        hidestringcheck.setBounds(690, 10, 100, 25);
        searchbar.setEchoChar((char)0);
        hidestringcheck.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == 1) {
                    searchbar.setEchoChar('*');
                } else {
                    searchbar.setEchoChar((char)0);
                }
            }
        });
        frame.add(hidestringcheck);
        //Search button
        JButton SearchButton = new JButton("Search");
        SearchButton.setBounds(795, 10, 80, 25);
        SearchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textarea.setText("");
                String methodtosearch = searchbar.getText();
                ArrayList<String> arraymethodsearch = new ArrayList<String>();
                arraymethodsearch.add("Searched-"+methodtosearch);
                for(ArrayList<String> ScanSetting: DecompiledData) {
                    Scan.ScanMethod(ScanSetting.get(0), arraymethodsearch, ScanSetting.get(1), ScanSetting.get(2), ScanSetting.get(3));
                }
            }


        });
        frame.add(SearchButton);
        //Make gui visible
        frame.setVisible(true);
    }

}
