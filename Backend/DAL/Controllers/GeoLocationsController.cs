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
using System.Web.Http.Cors;
using System.Web.Http.Description;

namespace DAL.Controllers
{
    [EnableCors(origins: "*", headers: "*", methods: "*")]
    public class GeoLocationsController : ApiController
    {
        IGeoLocationService locationService = new GeoLocationService();

        [HttpGet]
        public async Task<IHttpActionResult> Get()
        {
            try
            {
                var locations = await locationService.GetLocationsAsync();
                return Ok(locations.ToList());
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Get geolocations");
            }
        }


        [HttpGet]
        [CustomAttribute]
        //[Route("{id:int}")]
        public async Task<IHttpActionResult> GetById(int id)
        {
            try
            {
                var locations = await locationService.GetLocationsAsync();
                return Ok(locations.ToList().Where(c => c.CityID == id));
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u GetById geolocations");
            }
        }


    }
}
