using DAL.Interfaces;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace DAL.Services
{
    public class PictureService : IPictureService
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        Picture picture;

        public async Task<Picture> GetById(int id, int category)
        {
            var pictures = await db.Pictures.ToListAsync();
            
            switch (category)
            {
                case 1:
                   picture = pictures.Where(p => p.PopularLocationID == id).FirstOrDefault();
                    break;
                case 2:
                    picture = pictures.Where(p => p.AccomodationID == id).FirstOrDefault();
                    break;
                case 3:
                    picture = pictures.Where(p => p.WalletID == id).FirstOrDefault();
                    break;
                default:
                    break;
            }
            
            return picture;
        }

        public async Task<int> InsertAsync(Picture picture)
        {
            
            db.Pictures.Add(picture);
            await db.SaveChangesAsync();

            return picture.IDPicture;
            
        }
    }
}