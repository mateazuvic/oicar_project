using Microsoft.Owin.Security.OAuth;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Web.Http;
using System.Web.Http.Cors;

namespace DAL
{
    public static class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            // Web API configuration and services
            config.SuppressDefaultHostAuthentication();
            config.Filters.Add(new HostAuthenticationFilter(OAuthDefaults.AuthenticationType));
            //
            config.EnableCors();

            // Web API routes
            config.MapHttpAttributeRoutes();
            config.Formatters.JsonFormatter.SupportedMediaTypes
             .Add(new MediaTypeHeaderValue("text/html"));

            config.Formatters.XmlFormatter.SupportedMediaTypes
                .Add(new MediaTypeHeaderValue("multipart/form-data"));

            config.Formatters.XmlFormatter.SupportedMediaTypes
                .Add(new MediaTypeHeaderValue("text/plain"));

            config.Formatters.XmlFormatter.SupportedMediaTypes
                .Add(new MediaTypeHeaderValue("application/octet-stream"));




            //config.Routes.MapHttpRoute(
            //    name: "DefaultApi",
            //    routeTemplate: "api/{controller}/{id}",
            //    defaults: new { id = RouteParameter.Optional }
            //);


            config.Routes.MapHttpRoute(
             name: "DefaultApi",
             routeTemplate: "api/{controller}/{action}/{id}",
             defaults: new
             {
                 id = RouteParameter.Optional
             }
            );

            
        }
    }
}
