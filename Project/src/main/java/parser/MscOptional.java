package parser;

public class MscOptional {
    private String nome;
    private String diasem;
    private String per;

    public MscOptional(String nome, String diasem, String per) {
        this.nome = nome;
        this.diasem = diasem;
        this.per = per;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDiasem() {
        return diasem;
    }

    public void setDiasem(String diasem) {
        this.diasem = diasem;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }
}
