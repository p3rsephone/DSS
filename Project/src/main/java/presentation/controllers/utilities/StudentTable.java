package presentation.controllers.utilities;

public class StudentTable {
    private String numero;
    private String nome;
    private Integer faltas;

    public StudentTable(String numero, String nome, Integer faltas) {
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

    public Integer getFaltas() {
        return faltas;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }
}
