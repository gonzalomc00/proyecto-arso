
using System;
using System.Collections.Generic;


namespace Repositorio
{
    //Repositorio generico que permite la creacion de repositorios para cualquier tipo de entidad
    // Se notifican excepciones pero no se declara en la signatura aunque si aparece en la documentacion
    
    public interface Repositorio<T, K>
    {

        K Add(T entity);

        void Update(T entity);

        void Delete(T entity);

        T GetById(K id);

        List<T> GetAll();

        List<K> GetIds();
    }

}