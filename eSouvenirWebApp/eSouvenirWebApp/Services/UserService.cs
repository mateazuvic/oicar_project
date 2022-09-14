using eSouvenirWebApp.Interfaces;
using eSouvenirWebApp.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;


namespace eSouvenirWebApp.Services
{
    
    public class UserService : IUserService
    {
        private readonly HttpClient httpClient;
        private readonly JsonSerializerOptions options;

        

        public UserService(HttpClient httpClient)
        {
            this.httpClient = httpClient;
            options = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
        }

        public  async Task<IEnumerable<User>> GetUsers()
        {
            try
            {
                var response = await httpClient.GetAsync("api/users/get");
                var content = await response.Content.ReadAsStringAsync();
                if (!response.IsSuccessStatusCode)
                {
                    throw new Exception(content);
                }
                var users = JsonSerializer.Deserialize<List<User>>(content, options);
                return users;
            }
            catch (Exception e)
            {

                throw new Exception(e.Message);
            }
            

           
            



        }
    }
}
