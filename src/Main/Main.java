package Main;

import Resources.Finder;
import Resources.StatusListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main extends JFrame {

    private DefaultListModel<String> loaded = new DefaultListModel<>();
    private DefaultListModel<String> unloaded = new DefaultListModel<>();
    private ArrayList<Class> loadedClasses = new ArrayList<>();

    private JPanel contentPane;
    private JTextField txtPutThePath;
    private JTextField textField;
    private JTextField txtFoundClasses;
    private JTextField txtLoadedClasses;
    private JTextField txtPutTheParameter;
    private JTextField textField_1;
    private JTextField txtProgress;
    private JTextField textField_2;

    private String path;
    private String classPackage;
    private String newPath;
    File directory;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Main() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 740, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        txtPutThePath = new JTextField();
        txtPutThePath.setEditable(false);
        txtPutThePath.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtPutThePath.setText("Put the path to the directory with compiled classes:");
        txtPutThePath.setBounds(10, 10, 307, 19);
        contentPane.add(txtPutThePath);
        txtPutThePath.setColumns(10);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField.setBounds(10, 39, 307, 19);
        contentPane.add(textField);
        textField.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setEditable(false);
        textField_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField_2.setBounds(508, 96, 208, 45);
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        JList list = new JList(unloaded);
        list.setFont(new Font("Tahoma", Font.PLAIN, 12));
        list.setBounds(10, 96, 307, 107);
        contentPane.add(list);

        JList list_1 = new JList(loaded);
        list_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        list_1.setBounds(10, 238, 307, 107);
        contentPane.add(list_1);


        JButton btnFindClasses = new JButton("Find classes");
        btnFindClasses.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(textField.getText().equals("")){
                    JOptionPane.showMessageDialog(contentPane, "Put down the path!");
                }

                else{

                    unloaded.clear();

                    path = textField.getText();
                    String [] tab = path.split(":?\\\\");
                    classPackage = tab[tab.length-1];
                    newPath = "";
                    tab[0] = tab[0] +":";

                    for(int i=0; i < tab.length-1; i++){
                        newPath = newPath + tab[i] + "\\" + "\\";
                    }


                    Finder f =new Finder();
                    String[] files = f.findClasses(path);
                    for(String x : files){
                        x = x.replace(".class", "");
                        unloaded.addElement(x);
                    }

                    JOptionPane.showMessageDialog(contentPane, "All found compiled classes has been added.");
                    directory = new File(newPath);
                }

            }
        });
        btnFindClasses.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnFindClasses.setBounds(327, 10, 143, 45);
        contentPane.add(btnFindClasses);

        JButton btnNewButton = new JButton("Load class");
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(list.getSelectedIndex() == -1){
                    JOptionPane.showMessageDialog(contentPane, "Choose class to load!");
                }

                else{
                    loaded.addElement(unloaded.getElementAt(list.getSelectedIndex()));
                    try {
                        Path p = Paths.get(directory.toURI());
                        MyClassLoader myClassLoader = new MyClassLoader(p);
                        //URLClassLoader classLoader = new URLClassLoader(new URL[]{directory.toURI().toURL()});
                        //Class<?> loadedClass = classLoader.loadClass("Processors."+unloaded.get(list.getSelectedIndex()));
                        Class<?> loadedClass = myClassLoader.loadClass(classPackage+"."+unloaded.get(list.getSelectedIndex()));
                        loadedClasses.add(loadedClass);
                        unloaded.remove(list.getSelectedIndex());
                        myClassLoader = null;
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }


            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnNewButton.setBounds(327, 96, 143, 45);
        contentPane.add(btnNewButton);


        txtFoundClasses = new JTextField();
        txtFoundClasses.setText("Found classes:");
        txtFoundClasses.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtFoundClasses.setEditable(false);
        txtFoundClasses.setColumns(10);
        txtFoundClasses.setBounds(10, 68, 307, 19);
        contentPane.add(txtFoundClasses);

        txtLoadedClasses = new JTextField();
        txtLoadedClasses.setText("Loaded classes:");
        txtLoadedClasses.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtLoadedClasses.setEditable(false);
        txtLoadedClasses.setColumns(10);
        txtLoadedClasses.setBounds(10, 213, 307, 19);
        contentPane.add(txtLoadedClasses);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(508, 54, 208, 33);
        contentPane.add(progressBar);

        txtPutTheParameter = new JTextField();
        txtPutTheParameter.setText("Put the parameter:");
        txtPutTheParameter.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtPutTheParameter.setEditable(false);
        txtPutTheParameter.setColumns(10);
        txtPutTheParameter.setBounds(327, 214, 143, 19);
        contentPane.add(txtPutTheParameter);

        textField_1 = new JTextField();
        textField_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField_1.setColumns(10);
        textField_1.setBounds(327, 238, 143, 19);
        contentPane.add(textField_1);


        JButton btnRun = new JButton("Run");
        btnRun.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(list_1.getSelectedIndex()==-1){
                    JOptionPane.showMessageDialog(contentPane, "Choose class to run!.");
                }

                else if(textField_1.getText().equals("")){
                    JOptionPane.showMessageDialog(contentPane, "Put down the parameters!");
                }

                else{
                    progressBar.setValue(0);
                    textField_2.setText("");
                    Class<?> chosen = loadedClasses.get(list_1.getSelectedIndex());
                    try {
                        Constructor<?> constructor = chosen.getConstructor(JTextField.class);
                        Object object = constructor.newInstance(textField_2);
                        Method submitTaskMethod = chosen.getDeclaredMethod("submitTask", String.class, StatusListener.class);
                        boolean b = (boolean) submitTaskMethod.invoke(object, textField_1.getText(), new MyStatusListener(progressBar));
                        if(!b)
                            System.out.println("Something went wrong. :(");

                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (InstantiationException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }

                }


            }
        });
        btnRun.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnRun.setBounds(327, 267, 143, 45);
        contentPane.add(btnRun);

        txtProgress = new JTextField();
        txtProgress.setText("Progress:");
        txtProgress.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtProgress.setEditable(false);
        txtProgress.setColumns(10);
        txtProgress.setBounds(508, 11, 143, 19);
        contentPane.add(txtProgress);

        JButton btnUnloadClass = new JButton("Unload class");
        btnUnloadClass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(list_1.getSelectedIndex()==-1){
                    JOptionPane.showMessageDialog(contentPane, "Choose class to unload!");
                }

                else{
                    unloaded.addElement(loaded.get(list_1.getSelectedIndex()));
                    loadedClasses.remove(list_1.getSelectedIndex());
                    loaded.remove(list_1.getSelectedIndex());
                    System.gc();
                }

            }
        });
        btnUnloadClass.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnUnloadClass.setBounds(327, 322, 143, 45);
        contentPane.add(btnUnloadClass);

        JButton btnGetInfo = new JButton("Get info");
        btnGetInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(list_1.getSelectedIndex()==-1){
                    JOptionPane.showMessageDialog(contentPane, "Choose class!");
                }

                else{
                    Class<?> chosen = loadedClasses.get(list_1.getSelectedIndex());

                    try {
                        Constructor<?> constructor = chosen.getConstructor(JTextField.class);
                        Object object = constructor.newInstance(textField_2);
                        Method submitTaskMethod = chosen.getDeclaredMethod("getInfo");
                        String info = submitTaskMethod.invoke(object).toString();
                        JOptionPane.showMessageDialog(contentPane, info);

                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (InstantiationException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }


            }
        });
        btnGetInfo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnGetInfo.setBounds(10, 355, 143, 45);
        contentPane.add(btnGetInfo);
    }
}

