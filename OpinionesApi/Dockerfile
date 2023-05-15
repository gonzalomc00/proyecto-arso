#se descarga la imagen desde un registro de microsoft
FROM mcr.microsoft.com/dotnet/sdk:6.0 
#.Net hacerlo sobre sdk : mas rapido y directo - en terminos de produccion no tiene sentido pero en terminos de prueba SI
WORKDIR /app

#copio todos los recursos en /app
COPY . . 
# RUN dotnet dev-certs https -> deshabilitar puerto seguro
RUN dotnet build #construyo el proyecto 

#permite cambiar o reescribir los puertos de escucha de la aplicación (configuracion del proyecto)
ENV ASPNETCORE_KESTREL__ENDPOINTS__HTTP__URL=http://+:5000 
ENV MONGO_URI=

#dotnet run - comando que lo lanza
CMD ["dotnet", "run"]
#puerto para atender peticiones
EXPOSE 5000