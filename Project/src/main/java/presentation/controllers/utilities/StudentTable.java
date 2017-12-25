package presentation.controllers.utilities;

public class StudentTable {
    private String numero;
    private String nome;
    private String faltas;

    public StudentTable(String numero, String nome, String faltas) {
        this.numero = numero;
        this.nome = nome;
        this.faltas = faltas;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFaltas() {
        return faltas;
    }

    public void setFaltas(String faltas) {
        this.faltas = faltas;
    }
}
