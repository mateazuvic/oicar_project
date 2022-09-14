using DAL.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace DAL.Services
{
    public class AccommodationService : IAccommodationService
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        public async Task<AccomodationItem> InsertAsync(int qrId)
        {
            
            if (db.AccomodationItems.Any(c => c.QRCodeID == qrId))
            {
                return db.AccomodationItems.SingleOrDefault(c => c.QRCodeID == qrId);
            }
            else
            {
                AccomodationItem item = new AccomodationItem
                {
                    QRCodeID = qrId,
                    Pictures = new List<Picture>()
                };
                db.AccomodationItems.Add(item);
                await db.SaveChangesAsync();

                return item;

            }
        }
    }
}