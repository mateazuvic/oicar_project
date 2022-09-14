using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;

namespace DAL.Interfaces
{
    interface IPictureService
    {
        Task<int> InsertAsync(Picture picture);

        Task<Picture> GetById(int id, int category);
    }
}
