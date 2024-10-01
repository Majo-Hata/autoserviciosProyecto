package proyecto;

import java.util.*;
import proyecto.Materia;
import java.awt.*;

class Profesor{
  public Profesor(String id){
    this.id = id;
    this.materias = new Vector<MateriaProfesor>();
    Random rand = new Random();
    facilidad = rand.nextInt(10) + 1;
    calidad = rand.nextInt(10) + 1;
    while(materias.size() < 2){
      agregaMateria();
    }
    if(materias.elementAt(0).horario == materias.elementAt(1).horario){
      materias.elementAt(1).setHorario(rand.nextInt(19 - 7) + 7);
    }
  }
  private void agregaMateria(){
    int rand = new Random().nextInt(10);
    materias.add(new MateriaProfesor(Materia.materias[rand]));
  }
  private int facilidad;
  private int calidad;
  String id;
  public Vector<MateriaProfesor> materias;
  public int getFacilidad(){ return facilidad; }
  public int getCalidad(){ return calidad; }

  class MateriaProfesor{
    public MateriaProfesor(String nombre){
      this.materia = nombre;
      this.alumnos = new Vector<Alumno>();
      this.labelAlumnos = new Vector<Label>();
      this.horario = new Random().nextInt(19 - 7) + 7;
    }
    public String materia;
    int horario;
    int inscritos = 0;
    int cupo = 16;
    public Vector <Alumno> alumnos;
    public Vector<Label> labelAlumnos;

    public void setHorario(int h) { this.horario = h; }

    public synchronized boolean inscribeAlumno(Alumno a){
      if(cupo > 0){
        alumnos.add(a);
        labelAlumnos.elementAt(inscritos).setBackground(Color.red);
        labelAlumnos.elementAt(inscritos).setText("" + a.getAlumnoId());
        cupo -= 1;
        inscritos += 1;
        return true;
      }
      return false;
    }
  }
}
