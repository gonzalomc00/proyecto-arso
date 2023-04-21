using Opiniones.Modelo;
using Opiniones.Repositorio;
using Opiniones.Eventos;
using Repositorio;
using RabbitMQ.Client;
using System.Text.Json;
using System.Text;
using Microsoft.AspNetCore.Mvc;
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
        private string exchangeName;

        public ServicioOpiniones(Repositorio<Opinion, String> repositorio)
        {
            this.repositorio = repositorio;
            //Declaracion del exchange
            exchangeName = "evento.nueva.valoracion";

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
            evento.NuevaValoracion = new Valoracion()
            {
                Correo = correo,
                Fecha = fecha,
                Calificacion = calificacion,
                Comentario = comentario
            };
            evento.ResumenOpinion = new OpinionResumen()
            {
                CalificacionMedia = opinion.Media,
                NumValoraciones = opinion.NumValoraciones
            };

            notificarEvento(evento);

        }

        protected void notificarEvento(EventoNuevaValoracion evento)
        {

            //hacer conexion rara con el servicio de eventos
            try
            {
                ConnectionFactory factory = new ConnectionFactory();
                factory.Uri = new Uri("amqp://bmqyxhdb:t9WYA0qDTQpfRzSj5FJUHMwIp0mMatZm@whale.rmq.cloudamqp.com:5672/bmqyxhdb");
                //aqui no se crean tipos, ES VAR
                var connection = factory.CreateConnection();
                var channel = connection.CreateModel();

                Boolean durable = true;
                channel.ExchangeDeclare(exchangeName, ExchangeType.Fanout, durable);

                //Envio del mensaje
                var serializerSettings = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                string mensaje = JsonSerializer.Serialize(evento, serializerSettings);

                string routingKey = "arso";
                channel.BasicPublish(exchangeName, routingKey, null, Encoding.UTF8.GetBytes(mensaje));

                channel.Close();
                connection.Close();
            }
            catch
            {
                Console.WriteLine("Error al conectar con el servicio de eventos");
            }

        }

        public Opinion GetOpinionById(String id)
        {
            return this.repositorio.GetById(id);
        }
    }
}