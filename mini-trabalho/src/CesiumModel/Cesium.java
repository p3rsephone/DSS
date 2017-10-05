package CesiumModel;


import java.util.HashMap;
import java.util.Map;

import CesiumModel.Aluno;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

public class Cesium extends Observable implements Serializable{
    private HashMap<Integer, Aluno> cesium;
    private String date; 

    public Cesium(HashMap<Integer, Aluno> cesium) {
	this.cesium = cesium;
    }

    public Cesium() {
        cesium = new HashMap<>();
    }

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}


    

    public Map<Integer, Aluno> getAlunos() {
        HashMap<Integer, Aluno> copy = new HashMap<>();
        for (Map.Entry<Integer, Aluno> entry : cesium.entrySet())
        {
            copy.put(entry.getKey(), entry.getValue().clone());
        } 
        return copy;
    }

    public void updateQuotas(){

        for (Map.Entry<Integer, Aluno> entry : cesium.entrySet())
        {

	 String DATE_FORMAT_NOW = "dd-MM-yyyy";
	 Date date = new Date();
	 SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	 String stringDate = sdf.format(date );

	    entry.getValue().getValores().add(stringDate);
            cesium.put(entry.getKey(), entry.getValue());
	}
	   

        } 
    public void pagarQuota(Integer numero, Double valor){
        Aluno socio = cesium.get(numero);
	List a = socio.getValores();
        if(valor >= 5)
 		a.remove(0);
    }

    public Aluno getAluno(int num)
    {
        return cesium.get(num);
    }
    
    public void removeAluno(int num){
    	cesium.remove(num);
	this.setChanged();
	this.notifyObservers();
    }

    public void addAluno(Aluno a){
        cesium.put( a.getNumero() ,a.clone());
	this.setChanged();
	this.notifyObservers();
    }

}
