package business.utilities.graph;

import business.courses.Shift;

import java.util.HashMap;

public class Node {

    public final String shiftCode;
    public Shift shift;
    public final HashMap<Integer, Edge> inEdges;
    public final HashMap<Integer, Edge> outEdges;

    public Node(String shiftCode, Shift shift) {
        this.shiftCode = shiftCode;
        this.shift = shift;
        inEdges = new HashMap<>();
        outEdges = new HashMap<>();
    }

    public Node addEdge(Integer studentCode, Node dest){
        Edge e = new Edge(this, dest);
        outEdges.put(studentCode, e);
        dest.inEdges.put(studentCode, e);
        return this;
    }

    public void removeEdge(Integer studentCode) {
        if(outEdges.containsKey(studentCode)) {
            Node n = outEdges.get(studentCode).getTo();
            n.inEdges.remove(studentCode);
            outEdges.remove(studentCode);

        }
    }

    @Override
    public String toString() {
        return shiftCode;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public Shift getShift() {
        return shift;
    }
}

