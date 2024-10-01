package proyecto;

import java.applet.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.*;
import proyecto.Profesor;
import proyecto.Alumno;

class UI extends Frame{
  Button simulacionContinua = new Button("Simulación continua");
  Button simulacionPaso = new Button("Siguiente paso");
  Button reiniciar = new Button("Reiniciar");
  Button salir = new Button("Salir");
  Vector<Label> labelsProfs;
  Vector<Label> mats1;
  Vector<Label> mats2;
  Vector<Profesor> profesores;
  Vector<Alumno> alumnos;
  Autoservicios as;
  public void init(Vector<Alumno> a, Vector<Profesor> p, Autoservicios as){
    alumnos = a;
    profesores = p;
    this.as = as;
    labelsProfs = new Vector<Label>();
    mats1 = new Vector<Label>();
    mats2 = new Vector<Label>();
    for(int i = 0; i < p.size(); i++){
      labelsProfs.add(new Label(null, Label.CENTER));
      mats1.add(new Label(null, Label.CENTER));
      mats2.add(new Label(null, Label.CENTER));
    }
    setLayout(null);
    setBackground(Color.white);
    setForeground(Color.black);
    resize(1000, 1000);
    simulacionContinua.reshape(800, 40, 190, 25);
    simulacionContinua.setForeground(Color.black);
    simulacionContinua.setBackground(Color.white);
    simulacionPaso.reshape(800, 70, 190, 25);
    simulacionPaso.setForeground(Color.black);
    simulacionPaso.setBackground(Color.white);
    reiniciar.reshape(800, 100, 190, 25);
    reiniciar.setForeground(Color.black);
    reiniciar.setBackground(Color.white);
    salir.reshape(935, 970, 60, 25);
    salir.setForeground(Color.black);
    salir.setBackground(Color.white);
    as.alumnosInscritos.reshape(800, 130, 190, 25);
    as.alumnosInscritos.setForeground(Color.black);
    as.alumnosInscritos.setBackground(Color.lightGray);
    as.alumnosInscritos.setText("Inscritos: 0 / " + alumnos.size());
    as.turnoActual.reshape(800, 160, 190, 25);
    as.turnoActual.setForeground(Color.black);
    as.turnoActual.setBackground(Color.lightGray);
    as.turnoActual.setText("Turno actual: 0");
    as.alumnosCargaCompleta.reshape(800, 190, 190, 25);
    as.alumnosCargaCompleta.setForeground(Color.black);
    as.alumnosCargaCompleta.setBackground(Color.lightGray);
    as.alumnosCargaCompleta.setText("Con carga completa: 0");
    add(simulacionContinua);
    add(simulacionPaso);
    add(reiniciar);
    add(salir);
    add(as.alumnosInscritos);
    add(as.alumnosCargaCompleta);
    add(as.turnoActual);
    profs();
    initClases();
    as.start();
    show();
  }
  
  public boolean action(Event e, Object arg){
    if(as.modo.contentEquals("fin")){
      return true;
    }
    if(e.target == simulacionContinua){
      as.setModo("continuo");
      synchronized(as){ as.notify(); }
    } else if(e.target == simulacionPaso){
      as.setModo("paso");
      synchronized(as){
        as.notify();
      }
    } else if(e.target == reiniciar){
      Vector<Alumno> alumnos2 = new Vector<Alumno>();
      Vector<Profesor> profesores2 = new Vector<Profesor>();
      for(int i = 0; i < 5; i++){
        profesores2.add(new Profesor("" + i));
      }
      for(int i = 0; i < 200; i++){
        alumnos2.add(new Alumno(i, profesores2));
      }
      Autoservicios as2 = new Autoservicios(profesores2, alumnos2);
      removeAll();
      init(alumnos2, profesores2, as2);
      as2.setModo("detenido");
      synchronized(as2){
        as2.notify();
      }
    } else if(e.target == salir){
      System.exit(0);
    }
    return true;
  }

  private void profs(){
    int x = 10;
    int y = 50;
    int i = 0;
    for(Profesor p : profesores){
      Label lp = labelsProfs.elementAt(i);
      Label lm1 = mats1.elementAt(i);
      Label lm2 = mats2.elementAt(i);
      String textoLP = "Profesor " + p.id +
        " Calidad: " + p.getCalidad() +
        " Facilidad: " + p.getFacilidad();
      String textoLM1 = p.materias.get(0).materia + "- Hora: " + p.materias.get(0).horario;
      String textoLM2 = p.materias.get(1).materia + "- Hora: " + p.materias.get(1).horario;
      lp.setText(textoLP);
      lp.reshape(x, y, 200, 20);
      lp.setForeground(Color.black);
      add(lp);
      lm1.setText(textoLM1);
      lm1.reshape(x + 250, y, 230, 20);
      lm1.setForeground(Color.black);
      add(lm1);
      lm2.setText(textoLM2);
      lm2.reshape(x + 550, y, 230, 20);
      lm2.setForeground(Color.black);
      add(lm2);
      y += 190;
      i++;
    }
  }
  private void initClases(){
    int x = 320;
    int y = 80;
    for(Profesor p : profesores){
      for(int i = 0; i < 4; i++){
        for(int j = 0; j < 4; j++){
          Label l1 = new Label(null, Label.CENTER);
          l1.setForeground(Color.white);
          l1.setBackground(Color.green);
          l1.reshape(x, y, 35, 30);
          add(l1);
          p.materias.elementAt(0).labelAlumnos.add(l1);
          Label l2 = new Label(null, Label.CENTER);
          l2.setForeground(Color.white);
          l2.setBackground(Color.green);
          l2.reshape(x + 300, y, 35, 30);
          add(l2);
          p.materias.elementAt(1).labelAlumnos.add(l2);
          x += 35;
        }
        x = 320;
        y += 30;
      }
      y += 72;
    }
  }
}
