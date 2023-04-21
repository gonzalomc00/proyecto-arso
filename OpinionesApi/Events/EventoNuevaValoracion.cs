using Opiniones.Modelo;

namespace Opiniones.Eventos{
    public class EventoNuevaValoracion{
        public string IdOpinion { get; set; }
        public Valoracion NuevaValoracion { get; set; }
        public OpinionResumen ResumenOpinion { get; set; }
        
    }
}