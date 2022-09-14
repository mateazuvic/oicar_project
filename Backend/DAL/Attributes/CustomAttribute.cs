using DAL.Interfaces;
using DAL.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.Web.Http.Controllers;

namespace DAL.Attributes
{
    [AttributeUsage(AttributeTargets.Class | AttributeTargets.Method)]
    public class CustomAttribute : AuthorizeAttribute
    {
        JwtUtils jwtUtils = new JwtUtils();
        IUserService userService = new UserService();
        public override void OnAuthorization(HttpActionContext context)
        {
            try
            {
                var token = context.Request.Headers.GetValues("Authorization").FirstOrDefault()?.Split(' ').Last();

                var userId = jwtUtils.ValidateToken(token);
                if (userId != null)
                {
                    var u = userService.GetUser(userId.Value);
                    context.Request.Properties["User"] = u;
                    var user = (User)context.Request.Properties["User"];
                }
                else
                {
                    context.Response = new HttpResponseMessage(System.Net.HttpStatusCode.BadRequest)
                    {
                        Content = new StringContent("Wrong Token")
                    };
                }
               
               
            }
            catch (Exception)
            {
                context.Response = new HttpResponseMessage(System.Net.HttpStatusCode.BadRequest)
                {
                    Content = new StringContent("Missing Request Token")
                };
            }
           
        }
    }


   
        
}