using Opiniones.Modelo;
using Opiniones.Servicio;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System;

namespace OpinionesApi.Controllers
{
    [Route("api/opiniones")]
    [ApiController]
    public class OpinionesController : ControllerBase
    {
        private readonly IServicioOpiniones _servicio;

        public OpinionesController(IServicioOpiniones servicio)
        {
            this._servicio = servicio;
        }

        [HttpGet("{id}", Name = "GetOpinion")] //la misma notacion HttpGet ya lleva aparejado el path
        public ActionResult<Opinion> Get(String id)
        {
            var entidad = _servicio.GetOpinionById(id);
            if (entidad == null)
            {
                return NotFound();
            }
            return entidad;

        }

        //TODO: Revisar
        [HttpPost]
        public ActionResult<Opinion> Create([FromForm] string nombre)
        {

            string _id = _servicio.CreateOpinion(nombre);
            return CreatedAtRoute("GetOpinion", new { id = _id }, _servicio.GetOpinionById(_id));
        }

        [HttpDelete("{id}")]
        public IActionResult Delete(string id)
        { //Cuando una operación no retorna contenido declara el tipoIActionResult 

            var opinion = _servicio.GetOpinionById(id);
            if (opinion == null)
            {
                return NotFound();
            }
            this._servicio.DeleteOpinion(id);
            return NoContent();
        }

        /**[HttpPost("{id}/valoraciones")]
        public ActionResult addValoracion()
        {

        }*/

    }


}