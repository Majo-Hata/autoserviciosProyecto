package proyecto;

import java.util.Random;
import java.util.Vector;
import java.util.ArrayList;
import proyecto.Materia;
import proyecto.Profesor;
import proyecto.Profesor.MateriaProfesor;

class Alumno extends Thread {
  public Alumno(int id, Vector<Profesor> profesores){
    //Obtener un promedio entre 5 y 10
    promedio = 5 + (new Random().nextDouble()) * (10 - 5);
    this.id = id;
    proyeccion = new Proyeccion();
    materias = new Vector<MateriaAlumno>();
    horasInscritas = new Vector<Integer>();
    this.profesores = profesores;
    for(String m : proyeccion.getMaterias()){
      materias.add(new MateriaAlumno(m));
    }
  }

  public int getAlumnoId(){ return this.id; }

  public synchronized void run(){
    synchronized (this) {esperaTurno();}
    inscribeMateria();
  }

  public boolean cargaCompleta(){
    boolean resultado = true;
    for(MateriaAlumno ma : this.materias){
      resultado = ma.inscrita;
    }
    return resultado;
  }

  public void inscribeMateria(){
    for(MateriaAlumno ma : this.materias){
      boolean inscrita = false;
      for(MateriaProfesor mp : ma.ordenPreferencia){
        if(!horasInscritas.contains(mp.horario) && mp.inscribeAlumno(this)){
          horasInscritas.add(mp.horario);
          System.out.println("El alumno " + this.id + " inscribio " + mp.materia + " con horario de " + mp.horario);
          inscrita = true;
          ma.inscrita = true;
          break;
        }
      }
      if(!inscrita)
        System.out.println("El alumno " + this.id + " no pudo inscribir la materia " + ma.nombreMateria);
    }
  }

  synchronized void esperaTurno(){
    try{
      while(!leToca){
        synchronized(this){
          wait();
          break;
        }
      }
    } catch (InterruptedException e){}
  }

  double preferenciaProfesor(Profesor p){
    double resultado = 0;
    if(this.promedio > 8.5){
      resultado += p.getCalidad() * 1.5;
      resultado += p.getFacilidad();
    } else{
      resultado += p.getCalidad();
      resultado += p.getFacilidad() * 1.5;
    }
    return resultado;
  }

  public void setTurno(int turno){ this.turno = turno; }
  public int getTurno(){ return this.turno; }

  public double getPromedio() { return this.promedio; }

  public void setLeToca(boolean b) { this.leToca = b; }

  Vector<Profesor> profesores;
  double promedio;
  int id;
  int turno = 0;
  boolean leToca = false;
  Vector<Integer> horasInscritas;
  Proyeccion proyeccion;
  Vector<MateriaAlumno> materias;

  class MateriaAlumno{
    public MateriaAlumno(String nombre){
      this.nombreMateria = nombre;
      this.ordenPreferencia = new ArrayList<MateriaProfesor>();
    }

    boolean inscrita = false;
    double prefMaxima = 0.0;
    public ArrayList<MateriaProfesor> getOrden() { return this.ordenPreferencia; }
    public void ordenaProfesores(Vector<Profesor> profesores){
      for(Profesor p : profesores){
        for(MateriaProfesor mp : p.materias){
          if(mp.materia.contentEquals(nombreMateria)){
            if(ordenPreferencia.size() == 0){
              prefMaxima = preferenciaProfesor(p);
              ordenPreferencia.add(mp);
            }
            else if(preferenciaProfesor(p) > prefMaxima){
              ordenPreferencia.add(0, mp);
            }
          }
        }
      }
    }
    String nombreMateria;
    ArrayList<MateriaProfesor> ordenPreferencia;
  }

  class Proyeccion{
    public Proyeccion(){
      materias = new ArrayList<String>();
      while(materias.size() < 4){
        agregaMateria();
      }
    }

    private void agregaMateria(){
      int rand = new Random().nextInt(10);
      if(materias.contains(Materia.materias[rand])){
        return;
      } else{
        materias.add(Materia.materias[rand]);
      }
    }

    public ArrayList<String> getMaterias(){
      return this.materias;
    }

    private ArrayList<String> materias;
  }
}