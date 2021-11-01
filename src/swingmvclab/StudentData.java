package swingmvclab;

import swingmvclab.Student;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/*
 * A hallgatók adatait tároló osztály.
 */
public class StudentData extends AbstractTableModel {

    /*
     * Ez a tagváltozó tárolja a hallgatói adatokat.
     * NE MÓDOSÍTSD!
     */
    List<Student> students = new ArrayList<Student>();

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch(columnIndex) {
            case 0: return student.getName();
            case 1: return student.getNeptun();
            case 2: return student.hasSignature();
            default: return student.getGrade();
        }
    }

    @Override
    public String getColumnName(int index) {
        switch(index) {
            case 0: return "Név";
            case 1: return "Neptun";
            case 2: return "Aláírás";
            default: return "Jegy";
        }
    }
    @Override
    public Class<?> getColumnClass(int index) {
        Student s = students.get(0);
        switch(index) {
            case 0:
            case 1: return String.class;
            case 2: return Boolean.class;
            default: return Integer.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean[] b = {false,false,true,true};
        return (columnIndex<getColumnCount() && columnIndex>=0)?b[columnIndex]:false;
    }

    @Override
    public void setValueAt(Object o, int rowIndex, int columnIndex) {
        Student s = students.get(rowIndex);
        if(columnIndex>=2 && columnIndex <= 3) {
            switch(columnIndex) {
                case 2: s.setSignature((Boolean)o);
                        break;
                case 3: s.setGrade((Integer)o);
                        break;
            }
            students.set(rowIndex,s);
            this.fireTableRowsUpdated(rowIndex,rowIndex);
        }

    }

    public void addStudent(String name, String neptun) {
        students.add(new Student(name,neptun,Boolean.FALSE,0));
        this.fireTableDataChanged();
    }
}
