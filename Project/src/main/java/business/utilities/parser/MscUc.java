package business.utilities.parser;

class MscUc{
    private String nome;
    private String diasem;

    public MscUc(String nome, String diasem) {
        this.nome = nome;
        this.diasem = diasem;
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
}



