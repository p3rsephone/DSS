package business.courses.graph;

public class Edge {

    private final Node from;
    private final Node to;

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    public Node getFrom() {
        return this.from;
    }

    public Node getTo() {
        return this.to;
    }

    @Override
    public boolean equals(Object obj) {
        Graph.Edge e = (Graph.Edge)obj;
        return e.from == from && e.to == to;
    }
}
