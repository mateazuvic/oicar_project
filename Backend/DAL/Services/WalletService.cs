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
    public class WalletService : IWalletService
    {
        oicartim04dbEntities db = new oicartim04dbEntities();

        public async Task<IEnumerable<WalletItem>> GetAsync()
        {
            return await db.WalletItems.ToListAsync();
           
        }

        public async Task DeleteAsync(int id)
        {
            
            WalletItem wi = await db.WalletItems.FindAsync(id);
            var pictures = db.Pictures.Where(p => p.WalletID == wi.IDWalletItem).ToList();
            db.Pictures.RemoveRange(pictures);

            db.WalletItems.Remove(wi);
            await db.SaveChangesAsync();
        }

        public async Task<int> InsertAsync(WalletItem walletItem)
        {
                    
             WalletItem wi = new WalletItem
             {

                 Name = walletItem.Name,
                 Description = walletItem.Description,
                 Price = walletItem.Price,
                 QRCodeID = walletItem.QRCodeID

             };

            db.WalletItems.Add(wi);
            await db.SaveChangesAsync();

            if (walletItem.Pictures.Count > 0)
            {
                foreach (var item in walletItem.Pictures)
                {
                     Picture p = new Picture
                    {
                        Name = item.Name,
                        PicturePath = item.PicturePath,
                        WalletID = wi.IDWalletItem
                            
                    };
                    db.Pictures.Add(p);
                    await db.SaveChangesAsync();
                }

               
            }
         

            return wi.IDWalletItem;


        }
    }
}