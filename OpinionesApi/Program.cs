using System;
using Repositorio;
using Opiniones.Modelo;
using Opiniones.Repositorio;
using Opiniones.Servicio;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.OpenApi.Models;

namespace OpinionesApi
{
    public class Program
    {
        public static void Main(string[] args)
        {
            CreateHostBuilder(args).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder.UseStartup<Startup>();
                });
    }
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {

            //patron singleton para estas dependencias
            //configuracion rela de dependencias
            services.AddSingleton<Repositorio<Opinion, String>, RepositorioOpinionesMongoDB>(); //que repositorio darle

            services.AddSingleton<IServicioOpiniones, ServicioOpiniones>();  // qué servicio darle

            services.AddControllers(options =>
            {
                options.Filters.Add(typeof(ManejadorGlobalErrores));
            });

            services.AddControllers();
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "OpinionesApi", Version = "v1" });
            });
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "BookleApi v1"));
            }

            // app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}

public class ManejadorGlobalErrores : ExceptionFilterAttribute
{
    public override void OnException(ExceptionContext context)
    {
        base.OnException(context);

        if (context.Exception is ArgumentException)
        {
            context.Result = new BadRequestResult();
        }
    }
}