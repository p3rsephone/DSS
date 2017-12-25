package presentation.controllers.utilities;

public class CourseTable {
    private String uc;
    private String tp;
    private String trocaPendente;

    public CourseTable(String uc, String tp, String trocaPendente) {
        this.uc = uc;
        this.tp = tp;
        this.trocaPendente = trocaPendente;
    }

    public String getUc() {
        return uc;
    }

    public void setUc(String uc) {
        this.uc = uc;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getTrocaPendente() {
        return trocaPendente;
    }

    public void setTrocaPendente(String trocaPendente) {
        this.trocaPendente = trocaPendente;
    }
}
