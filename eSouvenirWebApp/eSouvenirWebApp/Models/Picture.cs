using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace eSouvenirWebApp.Models
{
    public class Picture
    {
        public int IDPicture { get; set; }
        public string Name { get; set; }
        public string PicturePath { get; set; }
        public int AccomodationID { get; set; }
        public int PopularLocationID { get; set; }
        public int WalletID { get; set; }
    }
}
