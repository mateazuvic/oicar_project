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
    public class PicturesController : ApiController
    {

        IPictureService picService = new PictureService();
        oicartim04dbEntities db = new oicartim04dbEntities();

        [HttpPost] 
        [CustomAttribute]
        public async Task<IHttpActionResult> Insert(Picture pic)
        {
            if (pic == null)
            {
                throw new Exception("Picture is null.");
            }
            try
            {
                int id = await picService.InsertAsync(pic);
                return Ok(id);
            }
            catch (Exception ex)
            {

                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Insert picture");

            }

        }


        [HttpGet]
        [Route("api/Pictures/GetbyId/{id}/{cat}")]
        public async Task<IHttpActionResult> GetById(int id, int cat)
        {
            try
            {
                var pic = await picService.GetById(id, cat);
                return Ok(pic);
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Getbyid picture");
                throw;
            }
        }

        [HttpGet]
      
        public async Task<IHttpActionResult> Get()
        {
            try
            {
                var pics = await db.Pictures.ToListAsync();
                return Ok(pics.ToList());
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Get picture");
                throw;
            }
        }
    }
}
