using DAL.Attributes;
using DAL.Dto;
using DAL.Interfaces;
using DAL.Log;
using DAL.Services;
using System;
using System.Net;
using System.Web.Http;
using System.Web.Http.Cors;

namespace DAL.Controllers
{

    [EnableCors(origins: "*", headers: "*", methods: "*")]
    public class LoginController : ApiController
    {

        private IUserService userService;

        public LoginController()
        {
            userService = new UserService();
        }


        
        [HttpPost]
        public IHttpActionResult Register(UserDto user)
        {
            try
            {
                int id = userService.Register(user);
                return Ok( id );//ako bude trebalo vratiti id
            }
            catch (Exception ex)
            {
              
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Register");

            }
           
        }

      

       
        [HttpPost]
        public User Login(UserDto user)
        {
            var response = userService.Authenticate(user);
            return response;
            
        }

        [HttpPost]
        public IHttpActionResult LoginAdmin(UserDto user)
        {           
            var response = userService.AuthenticateAdmin(user); 
            return Ok(response);

        }
    }
}