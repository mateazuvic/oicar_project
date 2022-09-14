using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Interfaces
{
    interface IWalletService
    {
        Task<IEnumerable<WalletItem>> GetAsync();

        Task DeleteAsync(int id);

        Task<int> InsertAsync(WalletItem walletItem);

       
    }
}
