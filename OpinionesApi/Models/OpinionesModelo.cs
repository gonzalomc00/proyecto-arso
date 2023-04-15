
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace Opiniones.Modelo
{
    public class Opinion
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string NombreRecurso { get; set; }
        public List<Valoracion> Valoraciones { get; set; } = new List<Valoracion>();

        [BsonElement]
        public int NumValoraciones => this.Valoraciones.Count;

        [BsonElement]
        public Double Media => calcularMedia();

        public double calcularMedia()
        {
            if (this.Valoraciones.Count == 0)
            {
                return 0;
            }
            else
            {
                double media = 0;
                foreach (Valoracion v in this.Valoraciones)
                {
                    media += v.Calificacion;
                }
                return media / this.Valoraciones.Count;
            }
        }
    }

    public class Valoracion
    {
        public string Correo { get; set; }
        public DateTime Fecha { get; set; }
        public double Calificacion { get; set; }
        public string Comentario { get; set; }
    }


    public class OpinionResumen
    {
        public double CalificacionMedia { get; set; }
        public int NumValoraciones { get; set; }
    }
}