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

namespace DAL.Controllers
{
    [EnableCors(origins: "*", headers: "*", methods: "*")]
    public class QRCodeController : ApiController
    {
        IQRCodeService qrService = new QRCodeService();
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        List<QRCode> codes = new List<QRCode>();

        public QRCodeController()
        {
        }

        public QRCodeController(List<QRCode> codes)
        {
            this.codes = codes;
        }


        [HttpPost]
        [CustomAttribute]
        public async Task<IHttpActionResult> Insert(QRCode qr)
        {
            try
            {
                log.Info(qr.IDQRCode.ToString());
                int id = await qrService.InsertAsync(qr);
                return Ok(id);
               
            }
            catch (Exception ex)
            {
                
                new LogWriter(ex.Message, ex.InnerException.Message);
                return Content(HttpStatusCode.BadRequest, "Greska u Insert qr code");

            }

        }

        [HttpGet]
        public async Task<IHttpActionResult> Get()
        {
           
            try
            {
                var qrCodes = await qrService.GetAsync();
                return Ok(qrCodes.ToList());
            }
            catch (Exception ex)
            {
                new LogWriter(ex.Message, ex.InnerException.StackTrace);
                return Content(HttpStatusCode.BadRequest, "Greska u Get qrcodes");
            }

        }
    }
}
