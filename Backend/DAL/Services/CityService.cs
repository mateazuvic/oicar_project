using DAL.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace DAL.Services
{
    public class CityService : ICityService
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        public async Task<int> InsertAsync(string name)
        {
            if (db.Cities.Any(c => c.Name == name))
            {
                return db.Cities.SingleOrDefault(c => c.Name == name).IDCity;
            }
            else
            {
                City city = new City
                {
                    Name = name
                };
                db.Cities.Add(city);
                await db.SaveChangesAsync();

                int id = city.IDCity;
                return id;

            }
        }
    }
}