package CesiumModel;

public class Aluno{
   private String name;
   private String contact;
   private Integer numero;
   private String curso;
   private String morada;
   private Integer ano;
   private Boolean quotas;

    public Aluno(String name, String contact, Integer numero, String curso, String morada, Integer ano, Boolean quotas) {
        this.name = name;
        this.contact = contact;
        this.numero = numero;
        this.curso = curso;
        this.morada = morada;
        this.ano = ano;
        this.quotas = quotas; // Just to know if allready paid lifetime membership
    }

    public Aluno(Aluno aluno) {

        this.name = aluno.getName();
        this.contact = aluno.getContact();
        this.numero = aluno.getNumero();
        this.curso = aluno.getCurso();
        this.morada = aluno.getMorada();
        this.ano = aluno.getAno();
        this.quotas = aluno.getQuotas();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Boolean isQuotas() {
        return quotas;
    }

    public void setQuotas(Boolean quotas) {
        this.quotas = quotas;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;

        Aluno a  = (Aluno) o;

        return this.getNumero().equals(a.getNumero());
    }

    @Override
    public Aluno clone() {
        return  new Aluno(this);
    }
}
