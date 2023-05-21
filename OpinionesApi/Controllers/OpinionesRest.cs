using Opiniones.Modelo;
using Opiniones.Servicio;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System;
using System.Text.Json;

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
        public ActionResult<Opinion> Get(string id)
        {
            Console.Write("eo");
            var entidad = _servicio.GetOpinionById(id);

            if (entidad == null)
            {
                return NotFound();
            }
            return entidad;

        }

        //TODO: Revisar
        [HttpPost]
        public ActionResult<Opinion> Create([FromBody] string nombre)
        {
            if (string.IsNullOrEmpty(nombre))
            {
                return BadRequest("Los parámetros proporcionados son inválidos.");
            }
            string _id = _servicio.CreateOpinion(nombre);
            return Content(JsonSerializer.Serialize(_id), "application/json");
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

        [HttpPost("{id}/valoraciones")]
        public IActionResult addValoracion(string id, [FromForm] string correo, [FromForm] double calificacion,
          [FromForm] string? comentario)
        {
            _servicio.addValoracion(id, correo, DateTime.Now, calificacion, comentario);

            if (string.IsNullOrEmpty(id) || string.IsNullOrEmpty(correo) || calificacion <= 0 || calificacion > 5)
            {
                return BadRequest("Los parámetros proporcionados son inválidos.");
            }
            
            return StatusCode(201);
        }
    }
}