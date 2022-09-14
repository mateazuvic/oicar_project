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
    public class PopularLocationsController : ApiController
    {
        IPopularLocationService plService = new PopularLocationService();
        oicartim04dbEntities db = new oicartim04dbEntities();

        [HttpGet]
        [CustomAttribute]
        [Route("api/PopularLocations/{geoId}/{qrId}")]
        public async Task<IHttpActionResult> Get(int geoId, int qrId)
        {
            try
            {
                var location = await plService.GetPopularLocationAsync(geoId, qrId);
                return Ok(location);
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Get poplocations");
            }
        }

        [HttpPost]
        [CustomAttribute]
        public async Task<IHttpActionResult> Update(PopularLocationItem item)
        {
            try
            {

                if (item == null)
                {
                    throw new Exception("Item je null.");
                }

                var existingItem = db.PopularLocationItems.Where(p => p.IDPopularLocationItem == item.IDPopularLocationItem).FirstOrDefault();
                if (existingItem != null)
                {
                    existingItem.Description = item.Description;
                    existingItem.GeoLocationID = item.GeoLocationID;
                    existingItem.QRCodeID = item.QRCodeID;
                    existingItem.IsVisited = item.IsVisited;

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
                    return Ok(item.IDPopularLocationItem);
                }

                return Content(HttpStatusCode.BadRequest, "Greska u updateanju!");
                
               
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Update popularlocations");
            }
        }


       
    }
}
