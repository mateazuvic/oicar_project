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
using System.Web.Http.Cors;

namespace DAL.Controllers
{
    [EnableCors(origins: "*", headers: "*", methods: "*")]
    public class CitiesController : ApiController
    {

        ICityService cityService = new CityService();
        oicartim04dbEntities db = new oicartim04dbEntities();

        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        List<City> cities = new List<City>();

        public CitiesController()
        {
        }

        public CitiesController(List<City> cities)
        {
            this.cities = cities;
        }

        [HttpGet]
        public async Task<IHttpActionResult> Get()
        {
           
            try
            {              
                var cities = await db.Cities.ToListAsync();
                return Ok(cities.ToList());
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Get cities");
            }


        }

        [HttpPost]
        [CustomAttribute]
        public async Task<IHttpActionResult> Insert(City c)
        {
            try
            {
                log.Info("Tu sam...");
                int id = await cityService.InsertAsync(c.Name);
                return Ok(id);
            }
            catch (Exception ex)
            {

                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Insert city");

            }

        }


    }

}
