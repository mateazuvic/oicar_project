using DAL.Attributes;
using DAL.Interfaces;
using DAL.Log;
using DAL.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Cors;

namespace DAL.Controllers
{
    [EnableCors(origins: "*", headers:"*",methods:"*")] 
    public class UsersController : ApiController
    {
        
        IUserService userService = new UserService();
        List<User> users = new List<User>();

        public UsersController()
        {
        }

        public UsersController(List<User> users)
        {
            this.users = users;
        }

        [HttpGet]
        public async Task<IHttpActionResult> Get()
        {
            try
            {
                var users = await userService.GetAllAsync();
                return Ok(users.ToList());
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.StackTrace);
                return Content(HttpStatusCode.BadRequest, "Greska u Get users");
            }
            

        }

        [HttpGet]
        public Task<User> GetById(int id)
        {
            var user = userService.GetById(id);
            return user;
        }

        [HttpPost]
        public IHttpActionResult UpdateUser(User user)
        {
            userService.Update(user);
            return Ok();
        }
    }
}
