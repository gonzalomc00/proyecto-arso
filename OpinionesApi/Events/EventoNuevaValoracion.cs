using Opiniones.Modelo;

namespace Opiniones.Eventos{
    public class EventoNuevaValoracion{
        public string IdOpinion { get; set; }
        public Valoracion nuevaValoracion { get; set; }
        public OpinionResumen resumenOpinion { get; set; }
        
    }
}