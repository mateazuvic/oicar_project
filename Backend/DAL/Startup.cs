using Microsoft.Owin;
using Owin;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Owin.Security.Jwt;
using Microsoft.Owin.Security.Authorization.Infrastructure;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using DAL.Attributes;
using Microsoft.Owin.Security;

[assembly: OwinStartup(typeof(DAL.Startup))]

namespace DAL
{
    public class Startup
    {
     
        
        public void Configuration(IAppBuilder app)
        {
            //app.UseMyCustomMiddleware();

 
            app.UseJwtBearerAuthentication(
               new JwtBearerAuthenticationOptions
               {
                   AuthenticationMode = AuthenticationMode.Active,
                   TokenValidationParameters = new TokenValidationParameters()
                   {
                       ValidateIssuer = true,
                       ValidateAudience = true,
                       ValidateIssuerSigningKey = true,
                       ValidIssuer = "https://localhost:44377/", //some string, normally web url,  
                       ValidAudience = "https://localhost:44377/",
                       IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes("my_secret_key_12345"))

                   }
               });

           

        }

       


    }
}
