using DAL;
using DAL.Controllers;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Http.Results;

namespace eSouvenirTest
{
    [TestClass]
    public class UsersTests
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        [TestMethod]
        public async Task GetAllUsers_ShouldReturnStatusCodeOk()
        {
            var testUsers = GetTestUsers();
            var controller = new UsersController(testUsers);

            var result = await controller.Get();
            Assert.IsInstanceOfType(result, typeof(OkNegotiatedContentResult<List<User>>));
        }

        private List<User> GetTestUsers()
        {
            List<User> users = new List<User>();
            db.Users.ToList().ForEach(c => users.Add(c));
            return users;

        }

        [TestMethod]
        public async Task GetUserById_ShouldReturnUser()
        {
            var controller = new UsersController();

            var result = await controller.GetById(1);
            Assert.IsNotNull(result);
            Assert.IsInstanceOfType(result, typeof(User));

        }

        [TestMethod]
        public void  UpdateUserById_ShouldReturnStatusCodeOk()
        {
            var controller = new UsersController();
            var test = new User { IDUser = 4, Username = "user4", Email = "user4@mail.com", IsAdmin = false, IsDeleted = false };
            var result = controller.UpdateUser(test);
            
            Assert.IsNotNull(result);
            Assert.IsInstanceOfType(result, typeof(OkResult));

        }
    }
}
