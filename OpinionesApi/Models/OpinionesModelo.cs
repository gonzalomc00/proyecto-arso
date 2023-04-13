
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
        public List<Valoracion> Valoraciones { get; set; }= new List<Valoracion>();
    }

    public class Valoracion
    {
        public string Correo { get; set; }
        public DateTime Fecha { get; set; }
        public double Calificacion { get; set; }
        public string Comentario { get; set; }
    }
}