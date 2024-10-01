package proyecto;
import proyecto.Alumno;
import proyecto.Profesor;
import java.util.Vector;
import proyecto.UI;
import proyecto.Autoservicios;

class Proyecto{
  public synchronized static void main(String[] args){
    Vector<Alumno> alumnos = new Vector<Alumno>();
    Vector<Profesor> profesores = new Vector<Profesor>();
    for(int i = 0; i < 5; i++){
      profesores.add(new Profesor("" + i));
    }
    for(int i = 0; i < 200; i++){
      alumnos.add(new Alumno(i, profesores));
    }
    UI ui = new UI();
    Autoservicios as = new Autoservicios(profesores, alumnos);
    ui.init(alumnos, profesores, as);
  }
}
