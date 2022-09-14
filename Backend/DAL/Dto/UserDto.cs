using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace DAL.Dto
{
    public class UserDto
    {
        public int IDUser { get; set; }
       
        public string Username { get; set; }
       
        public string Email { get; set; }
       
        public string Pass { get; set; }
       
        public bool IsAdmin { get; set; }

        public string Token { get; set; }

        public  ICollection<QRCode> QRCodes { get; set; }

        public override string ToString()
        {
            return Username + " ," + Pass + " ," + IDUser + " ," + IsAdmin + " ," + Email;
        }
    }
}