using DAL.Interfaces;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace DAL.Services
{
    public class GeoLocationService : IGeoLocationService
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        public async Task<IEnumerable<GeoLocation>> GetLocationsAsync()
        {
            return await db.GeoLocations.ToListAsync();
        }
    }
}