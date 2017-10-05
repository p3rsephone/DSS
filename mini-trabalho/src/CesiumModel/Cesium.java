package CesiumController;

import java.util.HashMap;
import java.util.Map;

import CesiumModel.Aluno;

public class Cesium {
    private HashMap<Integer, Aluno> cesium;

    public Cesium() {
        cesium = new HashMap<>();
    }

    public Map<Integer, Aluno> getAlunos() {
        HashMap<Integer, Aluno> copy = new HashMap<Integer, Aluno>();
        for (Map.Entry<Integer, Aluno> entry : cesium.entrySet())
        {
            copy.put(entry.getKey(), ((Aluno) entry).clone());
        }
        return copy;
    }


    public void pagarQuota(Integer numero, Double valor){
        Aluno socio = cesium.get(numero);
        if(valor >= 7.5)
            socio.setQuotas(true);

    }

    public Aluno getAluno(int num)
    {
        return cesium.get(num);
    }

    public void addAluno(Aluno a){
        cesium.put( a.getNumero() ,a.clone());
    }

}
