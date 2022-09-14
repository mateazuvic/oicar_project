﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Interfaces
{
    interface IQRCodeService
    {
        Task<int> InsertAsync(QRCode qr);

        Task<List<QRCode>> GetAsync();
    }
}
