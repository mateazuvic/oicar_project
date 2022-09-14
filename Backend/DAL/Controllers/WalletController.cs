using DAL.Attributes;
using DAL.Interfaces;
using DAL.Log;
using DAL.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;

namespace DAL.Controllers
{
    public class WalletController : ApiController
    {
        IWalletService walletService = new WalletService();
        oicartim04dbEntities db = new oicartim04dbEntities();

        [HttpGet]
        [CustomAttribute]
        public async Task<IHttpActionResult> Get()
        {
            try
            {
                var items = await walletService.GetAsync();
                return Ok(items);
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Get wallet");
            }
        }

        [HttpDelete]
        [CustomAttribute]
        public async Task<IHttpActionResult> Delete(int id)
        {
            if (id == 0)
            {
                throw new Exception("Id nema prihvatljivu vrijednost");
            }
            try
            {
                await walletService.DeleteAsync(id);
                return Ok("Deleted successfully");
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Delete wallet");
            }
        }

        [HttpPost]
        [CustomAttribute]
        public async Task<IHttpActionResult> Insert(WalletItem item)
        {
            if (item == null)
            {
                throw new Exception("Item je null.");
            }

            try
            {
               
                var id = await walletService.InsertAsync(item);
                return Ok(id);

            }
            catch (Exception ex)
            {

                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Insert wallet");

            }

        }

        [HttpGet]
        [CustomAttribute]
        [Route("api/Wallet/GetById/{id}")]
        public async Task<WalletItem> GetById(int id)
        {
            var item = await db.WalletItems.FindAsync(id);
            if (item == null)
            {
                throw new KeyNotFoundException("Wallet item not found");
            }
            return item;
        }

        [HttpPost]
        [CustomAttribute]
        public async Task<IHttpActionResult> Update(WalletItem item)
        {

            if (item == null)
            {
                throw new Exception("Item je null.");
            }

            try
            {

                var existingItem = db.WalletItems.Where(p => p.IDWalletItem == item.IDWalletItem).FirstOrDefault();
                if (existingItem != null)
                {
                    existingItem.Name = item.Name;
                    existingItem.Price = item.Price;
                    existingItem.Description = item.Description;
                    existingItem.QRCodeID = item.QRCodeID;

                    if (existingItem.Pictures.Count > 0 && item.Pictures.Count > 0)
                    {
                        if (existingItem.Pictures.ToList()[0].PicturePath != item.Pictures.ToList()[0].PicturePath)
                        {
                            existingItem.Pictures.ToList().ForEach(p => db.Pictures.Remove(p));
                            item.Pictures.ToList().ForEach(p => db.Pictures.Add(p));
                            db.SaveChanges();
                            existingItem.Pictures = item.Pictures;

                        }
                    }
                    else if (existingItem.Pictures.Count == 0 && item.Pictures.Count > 0)
                    {
                        existingItem.Pictures = item.Pictures;
                    }

                    await db.SaveChangesAsync();
                    return Ok(item.IDWalletItem);
                }

                return Content(HttpStatusCode.BadRequest, "Greska u updateanju!");


            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Update wallet");
            }
        }

    }
}
