
using Repositorio;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Linq;
using MongoDB.Bson;
using Opiniones.Modelo;
using System; // Para el Environment


//habria que programar el repositorio de manera casi generica
namespace Opiniones.Repositorio
{
    public class RepositorioOpinionesMongoDB : Repositorio<Opinion, string>
    {
        private readonly IMongoCollection<Opinion> opiniones; //implementacion de la interfaz

        public RepositorioOpinionesMongoDB()
        {
            
           //string uri =  Environment.GetEnvironmentVariable("MONGO_URI");
           string uri= "mongodb://sofia:sofia@ac-yfyrl7f-shard-00-00.68qbknn.mongodb.net:27017,ac-yfyrl7f-shard-00-01.68qbknn.mongodb.net:27017,ac-yfyrl7f-shard-00-02.68qbknn.mongodb.net:27017/?ssl=true&replicaSet=atlas-sbil5s-shard-0&authSource=admin&retryWrites=true&w=majority";

            //si una variable se va a inciar en su declaracion no hace falta ponerle el tipo --> inferencia de variables
            var client = new MongoClient(uri);
            var database = client.GetDatabase("ZeppelinUM");

            opiniones = database.GetCollection<Opinion>("opinion");
        }

        public string Add(Opinion entity)
        {
            opiniones.InsertOne(entity);

            return entity.Id;
        }

        public void Update(Opinion entity)
        {
            opiniones.ReplaceOne(opinion => opinion.Id == entity.Id, entity);
        }

        public void Delete(Opinion entity)
        {
            opiniones.DeleteOne(opinion => opinion.Id == entity.Id);
        }

        public List<Opinion> GetAll()
        {
            return opiniones.Find(_ => true).ToList();
        }

        public Opinion GetById(string id)
        {
            
            return opiniones.Find(opinion=>opinion.Id==id).FirstOrDefault();
        }

        public List<string> GetIds()
        {
            var todas =  opiniones.Find(_ => true).ToList();

            return todas.Select(p => p.Id).ToList();

        }
    }
}