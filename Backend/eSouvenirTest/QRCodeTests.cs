using DAL;
using DAL.Controllers;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Http.Results;

namespace eSouvenirTest
{
    [TestClass]
    public class QRCodeTests
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        [TestMethod]
        public async Task GetAllQRCodes_ShouldReturnStatusCodeOk()
        {
            var testCities = GetTestQRCodes();
            var controller = new QRCodeController(testCities);

            var result = await controller.Get();
            Assert.IsInstanceOfType(result, typeof(OkNegotiatedContentResult<List<QRCode>>));
        }

        private List<QRCode> GetTestQRCodes()
        {
            List<QRCode> codes = new List<QRCode>();
            db.QRCodes.ToList().ForEach(c => codes.Add(c));
            return codes;

        }

        [TestMethod]
        public async Task InsertQRCode_ShouldReturnStatusCodeOk()
        {
            var controller = new QRCodeController();

            var item = new QRCode { IDQRCode=111, CreationDate=DateTime.Now, UserID=1, CityID=1};

            var result = await controller.Insert(item);
            Assert.IsNotNull(result);
            Assert.IsInstanceOfType(result, typeof(OkNegotiatedContentResult<int>));

        }
    }
}
