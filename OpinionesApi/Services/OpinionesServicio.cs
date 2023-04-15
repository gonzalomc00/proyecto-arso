using Opiniones.Modelo;
using Opiniones.Repositorio;
using Opiniones.Eventos;
using Repositorio;

namespace Opiniones.Servicio
{
    public interface IServicioOpiniones
    {
        string CreateOpinion(String nombreRes);
        void DeleteOpinion(String idOpinion);
        void addValoracion(String idOpinion, String correo, DateTime fecha, double calificacion, String comentario);
        Opinion GetOpinionById(String id);
    }

    public class ServicioOpiniones : IServicioOpiniones
    {
        private Repositorio<Opinion, string> repositorio;

        public ServicioOpiniones(Repositorio<Opinion, String> repositorio)
        {
            this.repositorio = repositorio;
        }

        public string CreateOpinion(String nombreRes)
        {
            Opinion opinion = new Opinion();
            opinion.NombreRecurso = nombreRes;
            return this.repositorio.Add(opinion);
        }

        public void DeleteOpinion(String idOpinion)
        {
            this.repositorio.Delete(repositorio.GetById(idOpinion));
        }

        public void addValoracion(String idOp, String correo, DateTime fecha, double calificacion, String comentario)
        {
            Opinion opinion = repositorio.GetById(idOp);
            Boolean existe = false;


            //si un usuario ya ha valorado el recurso (restaurante), se actualiza su valoracion
            foreach (Valoracion v in opinion.Valoraciones)
            {
                if (v.Correo == correo)
                {
                    existe = true; //el usuario ya ha valorado el recurso
                    //reemplazamos los valores del recurso
                    v.Fecha = fecha;
                    v.Calificacion = calificacion;
                    v.Comentario = comentario;
                    repositorio.Update(opinion);
                }
            }

            //si no existe, se crea una nueva valoracion
            if (!existe)
            {
                opinion.Valoraciones.Add(new Valoracion()
                {
                    Correo = correo,
                    Fecha = fecha,
                    Calificacion = calificacion,
                    Comentario = comentario
                });

            }

            //comprobar si se actualiza la media
            repositorio.Update(opinion);

            //Notificar Evento

            //1. Crear el evento
            EventoNuevaValoracion evento = new EventoNuevaValoracion();
            evento.IdOpinion = idOp;
            evento.nuevaValoracion = new Valoracion()
            {
                Correo = correo,
                Fecha = fecha,
                Calificacion = calificacion,
                Comentario = comentario
            };
            evento.resumenOpinion = new OpinionResumen()
            {
                CalificacionMedia = opinion.Media,
                NumValoraciones = opinion.NumValoraciones
            };
            
            notificarEvento(evento);

        }

        protected void notificarEvento(EventoNuevaValoracion evento)
        {

            //hacer conexion rara con el servicio de eventos
        }

        public Opinion GetOpinionById(String id)
        {
            return this.repositorio.GetById(id);
        }
    }
}