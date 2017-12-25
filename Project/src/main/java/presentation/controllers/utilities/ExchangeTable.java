package presentation.controllers.utilities;

public class ExchangeTable {
    private String aluno1;
    private String aluno2;
    private String troca1;
    private String troca2;
    private Integer codigo;

    public ExchangeTable(String aluno1, String aluno2, String troca1, String troca2, Integer codigo) {
        this.aluno1 = aluno1;
        this.aluno2 = aluno2;
        this.troca1 = troca1;
        this.troca2 = troca2;
        this.codigo = codigo;
    }

    public String getAluno1() {
        return aluno1;
    }

    public void setAluno1(String aluno1) {
        this.aluno1 = aluno1;
    }

    public String getAluno2() {
        return aluno2;
    }

    public void setAluno2(String aluno2) {
        this.aluno2 = aluno2;
    }

    public String getTroca1() {
        return troca1;
    }

    public void setTroca1(String troca1) {
        this.troca1 = troca1;
    }

    public String getTroca2() {
        return troca2;
    }

    public void setTroca2(String troca2) {
        this.troca2 = troca2;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
