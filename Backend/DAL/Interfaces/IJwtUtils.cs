using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Interfaces
{
    public interface IJwtUtils
    {
        string GetToken(User user);
        int? ValidateToken(string token);
    }
}
