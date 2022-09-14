using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace eSouvenirWebApp.Models
{
    public class UserLogin
    {
        public int IDUser { get; set; }
        public string Username { get; set; }
        public string Email { get; set; }
        public string Pass { get; set; }
        public bool IsAdmin { get; set; }
        public bool IsDeleted { get; set; }

        public UserLogin(string username, string email, string pass, bool isAdmin, bool isDeleted)
        {
            Username = username;
            Email = email;
            Pass = pass;
            IsAdmin = isAdmin;
            IsDeleted = isDeleted;
        }
    }
}
