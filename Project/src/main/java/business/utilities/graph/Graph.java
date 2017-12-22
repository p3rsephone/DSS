package business.utilities.graph;

import business.courses.Exchange;
import business.courses.Shift;
import business.exceptions.RoomCapacityExceededException;
import business.exceptions.StudentAlreadyInShiftException;
import business.exceptions.StudentNotInShiftException;

import java.util.*;

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

    public Integer removeStudent(String shiftCode, Integer studentNumber) throws StudentNotInShiftException {
        Shift s = this.getShift(shiftCode);
        return s.removeStudent(studentNumber);
    }

    public void addRequest(Integer studentCode, String originShift, String destShift) {
        Node origin = this.getNode(originShift);
        Node dest = this.getNode(destShift);
        origin.addEdge(studentCode, dest);
    }

    public Set<Exchange> swap(String shiftCode) {
        Set<Exchange> res = new TreeSet<>();
        Node current = this.getNode(shiftCode);
        Set<Map.Entry<Integer, Edge>> in = current.getinEdges();
        Iterator<Map.Entry<Integer,Edge>> itori = in.iterator();
        while(itori.hasNext()) {
            Map.Entry<Integer, Edge> elem = itori.next();
            Integer student = elem.getKey();
            Node dest = elem.getValue().getTo();
            String shift = dest.getShiftCode();

            Set<Map.Entry<Integer, Edge>> out = dest.getinEdges();
            Iterator<Map.Entry<Integer,Edge>> itdest = in.iterator();
            while(itdest.hasNext()) {
                Map.Entry<Integer, Edge> el = itdest.next();
                Node candidate = el.getValue().getTo();
                if(candidate.getShiftCode().equals(shiftCode)) {
                    break;
                }
                Shift destination = candidate.getShift();
                Shift origin = current.getShift();
                current.removeEdge(student);
                candidate.removeEdge(el.getKey());
                Exchange ex = this.permute(origin, destination, student, el.getKey());
                if (ex != null) res.add(ex);
            }
        }
        return res;
    }

    public Exchange permute(Shift origin, Shift dest, Integer oriStudent, Integer destStudent) {
        Exchange res = null;
        try {
            dest.removeStudent(destStudent);
            origin.removeStudent(oriStudent);
            dest.addStudent(oriStudent);
            origin.addStudent(destStudent);
            res = new Exchange(origin.getCode(), dest.getCode(), oriStudent, destStudent);
        } catch (StudentAlreadyInShiftException | StudentNotInShiftException | RoomCapacityExceededException e) {
            e.printStackTrace();
        }
        return res;
    }
}
