using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace eSouvenirWebApp.Models
{
    public class City
    {
        public int IDCity { get; set; }
        public string Name { get; set; }
        
        public int NbrOfQrCodes { get; set; } = 0;
        public int NbrOfUsers { get; set; } = 0;

    }
}
