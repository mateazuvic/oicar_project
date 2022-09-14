using DAL.Controllers;
using DAL.Interfaces;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace DAL.Services
{
    public class PopularLocationService : IPopularLocationService
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        public async Task<PopularLocationItem> GetPopularLocationAsync(int geoId, int qrId)
        {
            List<PopularLocationItem> items = await db.PopularLocationItems.ToListAsync();
            var item = items.Where(p => p.GeoLocationID == geoId && p.QRCodeID == qrId).FirstOrDefault();

            if (item != null)
            {
                return item;
            }
            else
            {
                PopularLocationItem newItem = new PopularLocationItem()
                {
                    GeoLocationID = geoId,
                    QRCodeID = qrId,
                    IsVisited = false
                };
                db.PopularLocationItems.Add(newItem);
                db.SaveChanges();

                return newItem;
                
            }
            

        }

        public async Task<Picture> GetPicture(int id, int category)
        {
            var pictures = await db.Pictures.ToListAsync();
            Picture picture = new Picture();

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


    }
}