using Opiniones.Modelo;
using Opiniones.Repositorio;
using Repositorio;

namespace Opiniones.Servicio{
    public interface IServicioOpiniones{
        string CreateOpinion(String nombre);
        void DeleteOpinion(String idOpinion);
        void addValoracion(String idOpinion, String correo, DateTime fecha, double calificacion, String comentario);
        Opinion GetOpinionById(String id);
    }

    public class ServicioOpiniones : IServicioOpiniones{
        private Repositorio<Opinion, string> repositorio;

        public ServicioOpiniones(Repositorio<Opinion, String> repositorio){
            this.repositorio = repositorio;
        }

        public string CreateOpinion(String nombre){
            Opinion opinion = new Opinion();
            opinion.NombreRecurso = nombre;
            return this.repositorio.Add(opinion);
        }

        public void DeleteOpinion(String idOpinion){
            this.repositorio.Delete(repositorio.GetById(idOpinion));
        }

        public void addValoracion(String idOpinion, String correo, DateTime fecha, double calificacion, String comentario){
            Opinion opinion = repositorio.GetById(idOpinion);
            opinion.Valoraciones.Add(new Valoracion(){
                Correo = correo,
                Fecha = fecha,
                Calificacion = calificacion,
                Comentario = comentario
            });
            repositorio.Update(opinion);
        }

        public Opinion GetOpinionById(String id){
            return this.repositorio.GetById(id);
        }
    }
}