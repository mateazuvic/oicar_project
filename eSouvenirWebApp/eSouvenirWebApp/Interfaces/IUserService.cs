using eSouvenirWebApp.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace eSouvenirWebApp.Interfaces
{
    public interface IUserService
    {
        Task<IEnumerable<User>> GetUsers();
    }
}
