using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace eSouvenirWebApp.Models
{

    //Bug fixed
    public class QRCode
    {
        public int IDQRCode { get; set; }
        public DateTime? CreationDate { get; set; }
        public int UserID { get; set; }
        public int CityID { get; set; }
    }
}
