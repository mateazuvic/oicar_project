using DAL.Attributes;
using DAL.Interfaces;
using DAL.Log;
using DAL.Services;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;

namespace DAL.Controllers
{
    public class AccommodationController : ApiController
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        IAccommodationService accService = new AccommodationService();

        [HttpGet]
        [CustomAttribute]
        public async Task<IHttpActionResult> Get()
        {           
            try
            {
                var acc = await db.AccomodationItems.ToListAsync();
                return Ok(acc.ToList());
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Get acommodation");
            }

        }

        [HttpPost]
        [CustomAttribute]
        [Route("api/Accommodation/insert/{qrId}")]
        public async Task<IHttpActionResult> Insert(int qrId)
        {
            try
            {
                
                var item = await accService.InsertAsync(qrId);
                return Ok(item);
            }
            catch (Exception ex)
            {

                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Insert accommodation");

            }

        }

        [HttpPost]
        [CustomAttribute]
        public async Task<IHttpActionResult> Update(AccomodationItem item)
        {

            if (item == null)
            {
                throw new Exception("Item je null.");
            }

            try
            {

                var existingItem = db.AccomodationItems.Where(p => p.IDAccomodationItem == item.IDAccomodationItem).FirstOrDefault();
                if (existingItem != null)
                {
                    existingItem.Name = item.Name;
                    existingItem.Link = item.Link;
                    existingItem.Description = item.Description;
                    existingItem.QRCodeID = item.QRCodeID;

                    if (existingItem.Pictures.Count > 0 && item.Pictures.Count > 0)
                    {
                        if (existingItem.Pictures.ToList()[0].PicturePath != item.Pictures.ToList()[0].PicturePath)
                        {
                            existingItem.Pictures.ToList().ForEach(p => db.Pictures.Remove(p));
                            existingItem.Pictures = item.Pictures;

                        }
                    }
                    else if (existingItem.Pictures.Count == 0 && item.Pictures.Count > 0)
                    {
                        existingItem.Pictures = item.Pictures;
                    }

                    await db.SaveChangesAsync();
                    return Ok(item.IDAccomodationItem);
                }

                return Content(HttpStatusCode.BadRequest, "Greska u updateanju!");


            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Update accomodation");
            }
        }

    }
}
