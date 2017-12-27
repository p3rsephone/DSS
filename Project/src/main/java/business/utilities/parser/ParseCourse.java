package business.utilities.parser;

public class ParseCourse {
    private String codigo;
    private String nome;
    private int teacherReg;
    private int year;

    public ParseCourse(String codigo, String nome, int teacherReg, int year) {
        this.codigo = codigo;
        this.nome = nome;
        this.teacherReg = teacherReg;
        this.year = year;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTeacherReg() {
        return teacherReg;
    }

    public void setTeacherReg(int teacherReg) {
        this.teacherReg = teacherReg;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
