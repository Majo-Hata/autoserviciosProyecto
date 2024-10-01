package proyecto;

import java.awt.Label;
import java.util.*;
import java.util.Comparator.*;
import proyecto.Alumno;
import proyecto.Alumno.MateriaAlumno;

class Autoservicios extends Thread{
  int actual = 0;
  Vector<Profesor> profesores;
  Vector<Alumno> alumnos;
  String modo;
  int cargaCompleta = 0;
  int inscritos = 0;
  Label alumnosCargaCompleta;
  Label alumnosInscritos;
  Label turnoActual;

  public Autoservicios(Vector<Profesor> profesores, Vector<Alumno> alumnos){
    this.profesores = profesores;
    this.alumnos = alumnos;
    this.modo = "detenido";
    this.alumnosCargaCompleta = new Label(null, Label.CENTER);
    this.alumnosInscritos = new Label(null, Label.CENTER);
    this.turnoActual = new Label(null, Label.CENTER);
  }

  public void setModo(String m) { this.modo = m; }
  public int getCargaCompleta() { return this.cargaCompleta; }
  public int getInscritos() { return this.inscritos; }
  public synchronized void run(){
    try{
      asignaTurno();
      for(Alumno a : alumnos){
        for(MateriaAlumno ma : a.materias){
          ma.ordenaProfesores(profesores);
        }
        a.start();
      }
      while(!modo.contentEquals("salir")){
        if(modo.contentEquals("continuo")){
          while(actual < 20){
            siguienteTurno();
            sleep(100);
          }
          modo = "detenido";
          synchronized(this) { wait(); }
        } else if(modo.contentEquals("paso")){
          siguienteTurno();
          modo = "detenido";
        } else{
          synchronized(this){
            wait();
          }
        }
      }
    } catch (InterruptedException e){ }
  }

  public void asignaTurno(){
    alumnos.sort(Comparator.comparing(Alumno::getPromedio).reversed());
    for(int i = 0; i < alumnos.size(); i++){
      int j = Math.floorDiv(i, 10);
      alumnos.elementAt(i).setTurno(j);
    }
  }

  int ins = 0;
  synchronized void siguienteTurno() throws InterruptedException{
    if(actual >= 20)
      return;
    
    for(Alumno a : alumnos){
      if(a.getTurno() == actual){
        synchronized(a){
          a.setLeToca(true);
          a.notify();
        }
        ins += 1;
      }
    }
    actual += 1;
    int cc = 0;
    for(Alumno a : alumnos){
      if(a.cargaCompleta()){
        cc += 1;
      }
    }
    alumnosCargaCompleta.setText("Carga completa: " + cc);
    alumnosInscritos.setText("Alumnos inscritos: " + ins + " / " + alumnos.size());
    turnoActual.setText("Turno actual: " + actual);
  }
}
