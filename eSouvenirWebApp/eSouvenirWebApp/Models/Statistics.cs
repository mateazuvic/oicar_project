using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace eSouvenirWebApp.Models
{
    public class Statistics
    {
        public string Username { get; set; }
        //public string City { get; set; }
        //public bool IsAdmin { get; set; }


        //ovo je Jolin komentar za statistiku
        public int NbrOfPictures { get; set; }

        public int NbrOfVisitedCities { get; set; }
    }

    public class StatisticsData : List<Statistics>
    {
        public StatisticsData()
        {
            Add(new Statistics { Username = "Luka", NbrOfPictures=9, NbrOfVisitedCities=1 });
            Add(new Statistics { Username = "Jole", NbrOfPictures = 19, NbrOfVisitedCities=4 });
            Add(new Statistics { Username = "Matea", NbrOfPictures = 9,  NbrOfVisitedCities = 2 });
            Add(new Statistics { Username = "Domi", NbrOfPictures = 7, NbrOfVisitedCities = 17 });
            Add(new Statistics { Username = "Petar", NbrOfPictures = 12, NbrOfVisitedCities = 5 });
            Add(new Statistics { Username = "Dario", NbrOfPictures = 13, NbrOfVisitedCities = 4 });
        }
    }
}
