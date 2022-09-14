using DAL.Interfaces;
using DAL.Log;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace DAL.Services
{
    public class QRCodeService : IQRCodeService
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        public async Task<int> InsertAsync(QRCode qr)
        {
           
            log.Info("QR: " + qr.ToString());
            if (qr == null)
            {
                log.Error("Nema qr koda u Insert");
                return -3;
            }
            if (db.QRCodes.Any(c => c.IDQRCode == qr.IDQRCode && c.CityID==qr.CityID))
            {
                return db.QRCodes.SingleOrDefault(c => c.IDQRCode == qr.IDQRCode && c.CityID==qr.CityID).IDQRCode;
            }
            else
            {
                try
                {
                     QRCode q = new QRCode
                     {
                        IDQRCode=qr.IDQRCode,
                        CreationDate = DateTime.Now,
                        UserID = qr.UserID,
                        CityID = qr.CityID

                     };
                    
                    log.Warn("Q: "+ q.ToString());

                    db.QRCodes.Add(q);
                    await db.SaveChangesAsync();
                    int id = q.IDQRCode;
                    return id;
                }
                catch (Exception ex)
                {

                    new LogWriter(ex.Message, ex.InnerException.Message);
                    return -1;
                }

                

            }
        }

        public async Task<List<QRCode>> GetAsync()
        {
            try
            {
                return await db.QRCodes.ToListAsync();
            }
            catch (Exception ex)
            {

                new LogWriter(ex.Message, ex.InnerException.Message);
                return null;
            }
        }
    }
    
}