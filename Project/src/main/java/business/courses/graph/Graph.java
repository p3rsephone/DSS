package business.courses.graph;

import business.courses.Shift;
import business.exceptions.RoomCapacityExceededException;
import business.exceptions.StudentAlreadyInShiftException;
import business.exceptions.StudentNotInShiftException;
import business.users.Student;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Graph {

    private HashSet<Node> shifts;

    public Graph() {
        this.shifts = new HashSet<>();
    }

    public void addShift(Shift shift) {
        Node n = new Node(shift.getCode(), shift);
        this.shifts.add(n);
    }

    public Shift getShift(String shiftCode) {
        Iterator<Node> it = this.shifts.iterator();
        HashMap<String, Shift> sh = new HashMap<>();
        Shift res = null;
        while (it.hasNext()) {
            Node currentNode = it.next();
            if (currentNode.getShiftCode().equals(shiftCode)) {
                res = currentNode.getShift();
                break;
            }
        }

        return res;
    }

    public Node getNode(String shiftCode) {
        Iterator<Node> it = this.shifts.iterator();
        HashMap<String, Shift> sh = new HashMap<>();
        Node res = null;
        while (it.hasNext()) {
            Node currentNode = it.next();
            if (currentNode.getShiftCode().equals(shiftCode)) {
                res = currentNode;
                break;
            }
        }

        return res;
    }

    public HashMap<String,Shift> getShifts() {
        Iterator<Node> it = this.shifts.iterator();
        HashMap<String, Shift> sh = new HashMap<>();
        while(it.hasNext()) {
            Node currentNode = it.next();
            sh.put(currentNode.getShiftCode(), currentNode.getShift());
        }
        return sh;
    }

    public void addStudent(String shiftCode, Integer studentNumber) throws RoomCapacityExceededException, StudentAlreadyInShiftException {
        Iterator<Node> it = this.shifts.iterator();
        HashMap<String, Shift> sh = new HashMap<>();
        Shift res = null;
        while (it.hasNext()) {
            Node currentNode = it.next();
            if (currentNode.getShiftCode().equals(shiftCode)) {
                res = currentNode.getShift();
                break;
            }
        }
        res.addStudent(studentNumber);
    }

    public boolean removeStudent(String shiftCode, Integer studentNumber) throws StudentNotInShiftException {
        Shift s = this.getShift(shiftCode);
        return s.removeStudent(studentNumber);
    }

    public void addRequest(Integer studentCode, String originShift, String destShift) {
        Node origin = this.getNode(originShift);
        Node dest = this.getNode(destShift);
        origin.addEdge(studentCode, dest);
    }
}
