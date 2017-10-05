package CesiumModel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Aluno implements Serializable{
   private String name;
   private String contact;
   private Integer numero;
   private String curso;
   private String morada;
   private Integer ano;
   private List valores;

    public Aluno(String name, String contact, Integer numero, String curso, String morada, Integer ano, Boolean quotas, List valores) {
        this.name = name;
        this.contact = contact;
        this.numero = numero;
        this.curso = curso;
        this.morada = morada;
        this.ano = ano;
	this.valores = valores;
    }

    public Aluno(Aluno aluno) {

        this.name = aluno.getName();
        this.contact = aluno.getContact();
        this.numero = aluno.getNumero();
        this.curso = aluno.getCurso();
        this.morada = aluno.getMorada();
        this.ano = aluno.getAno();
	this.valores = aluno.getValores();
    }


	public Aluno() {
        this.name = "";
        this.contact = "";
        this.numero = 0;
        this.curso = "";
        this.morada = "";
        this.ano = 0;
	this.valores = new ArrayList<String>() ; 
	 String DATE_FORMAT_NOW = "dd-MM-yyyy";
	 Date date = new Date();
	 SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	 String stringDate = sdf.format(date );
	    try {
		Date date2 = sdf.parse(stringDate);
	    } catch(ParseException e){
	     //Exception handling
	    } catch(Exception e){
	     //handle exception
	    }
 	this.valores.add(stringDate);
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


    public List getValores() {
	return valores;
    }

    public void setValores(List valores) {
	this.valores = valores;
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
