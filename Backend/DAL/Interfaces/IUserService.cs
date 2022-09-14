using DAL.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Interfaces
{
    public interface IUserService
    {
        Task<IEnumerable<User>> GetAllAsync();
        Task<User> GetById(int id);
        int Register(UserDto user);
        User Authenticate(UserDto user);

        int AuthenticateAdmin(UserDto user);

        void Update(User user);
        User GetUser(int value);

        

        
    }
}
