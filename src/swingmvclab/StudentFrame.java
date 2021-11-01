package swingmvclab;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

/*
 * A megjelenítendõ ablakunk osztálya.
 */
public class StudentFrame extends JFrame {

    /*
     * Ebben az objektumban vannak a hallgatói adatok.
     * A program indulás után betölti az adatokat fájlból, bezáráskor pedig kimenti oda.
     *
     * NE MÓDOSÍTSD!
     */
    private StudentData data;

    /*
     * Itt hozzuk létre és adjuk hozzá az ablakunkhoz a különbözõ komponenseket
     * (táblázat, beviteli mezõ, gomb).
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());

        JTable jt = new JTable();
        jt.setModel(data);
        jt.setFillsViewportHeight(rootPaneCheckingEnabled);
        jt.setRowSorter(new TableRowSorter<StudentData>(data));

        jt.setDefaultRenderer(String.class, new StudentTableCellRenderer(jt.getDefaultRenderer(String.class)));
        jt.setDefaultRenderer(Boolean.class, new StudentTableCellRenderer(jt.getDefaultRenderer(Boolean.class)));
        jt.setDefaultRenderer(Number.class, new StudentTableCellRenderer(jt.getDefaultRenderer(Number.class)));

        this.add(new JScrollPane(jt),BorderLayout.CENTER);

        JTextField newName;
        JTextField newNeptun;
        JPanel adderPanel = new JPanel(new FlowLayout());
        adderPanel.add(new JLabel("Név:"));
        newName = (JTextField) adderPanel.add(new JTextField(20));

        adderPanel.add(new JLabel("Neptun:"));
        newNeptun = (JTextField) adderPanel.add(new JTextField(6));

        JButton adderButton = (JButton) adderPanel.add(new JButton("Felvesz"));
        adderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data.addStudent(newName.getText(), newNeptun.getText());
                jt.updateUI();
            }
        });

        this.add(adderPanel,BorderLayout.SOUTH);
    }

    /*
     * Az ablak konstruktora.
     *
     * NE MÓDOSÍTSD!
     */
    @SuppressWarnings("unchecked")
    public StudentFrame() {
        super("Hallgatói nyilvántartás");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Induláskor betöltjük az adatokat
        try {
            data = new StudentData();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"));
            data.students = (List<Student>)ois.readObject();
            ois.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        // Bezáráskor mentjük az adatokat
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"));
                    oos.writeObject(data.students);
                    oos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Felépítjük az ablakot
        setMinimumSize(new Dimension(500, 200));
        initComponents();
    }

    /*
     * A program belépési pontja.
     *
     * NE MÓDOSÍTSD!
     */
    public static void main(String[] args) {
        // Megjelenítjük az ablakot
        StudentFrame sf = new StudentFrame();
        sf.setVisible(true);
    }

    private class StudentTableCellRenderer implements TableCellRenderer {
        private final TableCellRenderer renderer;
        public StudentTableCellRenderer(TableCellRenderer defRenderer) {
            this.renderer = defRenderer;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Student actualStudent = data.students.get(table.getRowSorter().convertRowIndexToModel(row));
            Color bg;
            if(actualStudent.getGrade()<=1)
                bg=Color.RED;
            else
                bg=Color.GREEN;
            component.setBackground(bg);

            return component;
        }
    }
}
