using DAL;
using DAL.Controllers;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web.Http.Results;

namespace eSouvenirTest
{
    [TestClass]
    public class CitiesTests
    {
        oicartim04dbEntities db = new oicartim04dbEntities();

        [TestMethod]
        public async Task GetAllCities_ShouldReturnStatusCodeOk()
        {
            var testCities = GetTestCities();
            var controller = new CitiesController(testCities);

            var result = await controller.Get();
            Assert.IsInstanceOfType(result, typeof(OkNegotiatedContentResult<List<City>>));
        }

        private List<City> GetTestCities()
        {
            List<City> cities = new List<City>();
            db.Cities.ToList().ForEach(c => cities.Add(c));
            return cities;

        }

        [TestMethod]
        public async Task InsertCity_ShouldReturnStatusCode()
        {
            var controller = new CitiesController();

            var item = new City { IDCity = 111, Name="Zagreb"};

            var result = await controller.Insert(item);
            Assert.IsNotNull(result);
            Assert.IsInstanceOfType(result, typeof(OkNegotiatedContentResult<int>));
            
        }


    }
}
