using DAL.Dto;
using DAL.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Data.Entity;
using DAL.Log;
using System.IdentityModel.Tokens.Jwt;

namespace DAL.Services
{
    public class UserService : IUserService
    {
        oicartim04dbEntities db = new oicartim04dbEntities();
        JwtUtils jwt = new JwtUtils();
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        private static Random random = new Random();

        public User Authenticate(UserDto userDto)
        {
            //1. pretvori pass u hash
            var hashPass = ConvertToHash(userDto.Pass);

            //2. nadi usera u bazi
            var user = db.Users.SingleOrDefault(u => u.Username == userDto.Username);
            if (user == null || hashPass.Length != user.Pass.Length)
            {
                return new User() { IDUser = -1 };
                //throw new Exception("Username or password is incorrect!");
            }
            else if (hashPass.Length == user.Pass.Length)
            {
                for (int i = 0; i < hashPass.Length; i++)
                {
                    if (hashPass[i] != user.Pass[i])
                    {
                        return new User() { IDUser = -1 };
                        // throw new Exception("Password is incorrect!");
                    }
                }
            }

            //3. getToken za usera, treba li ga spremiti???
            string token = jwt.GetToken(user);

            user.Token = token;
            return user;

        }

        public int AuthenticateAdmin(UserDto userDto)
        {
            
            var hashPass = ConvertToHash(userDto.Pass);

            var user = db.Users.SingleOrDefault(u => u.Username == userDto.Username && u.IsAdmin == true);
            if (user == null || hashPass.Length != user.Pass.Length)
            {
                return -1;
                throw new Exception("Username or password is incorrect!");
                
            }
            else if (hashPass.Length == user.Pass.Length)
            {
                for (int i = 0; i < hashPass.Length; i++)
                {
                    if (hashPass[i] != user.Pass[i])
                    {
                        return -1;
                        throw new Exception("Password is incorrect!");
                    }
                }
            }
           
            return user.IDUser;

        }

        public async Task<IEnumerable<User>> GetAllAsync()
        {
            
            try
            {
                return await db.Users.ToListAsync();
            }
            catch (Exception ex)
            {

                new LogWriter(ex.Message, ex.InnerException.Message);
                return null;
            }
        }

        public async Task<User> GetById(int id)
        {

            var user = await db.Users.FindAsync(id);
            if (user == null)
            {
                throw new KeyNotFoundException("User not found");
            }
            return user;

        }

        public int Register(UserDto user)
        {
            //1. je li username ili email vec zauzet?
            if (db.Users.Any(u => u.Username == user.Username))
            {
                //throw new Exception("Username is already taken!");         
                return -1;
            }
            else if(db.Users.Any(u => u.Email == user.Email))
            {
                //throw new Exception("Email is already taken!"); 
                return -2;
            }

            //2. pass hashirat
            byte[] hashPass = ConvertToHash(user.Pass);

            //3. trebamo ubaciti u bazu
            User us = new User
            {
                Username=user.Username,
                Email=user.Email,
                Pass=hashPass,
                IsAdmin=false,
                IsDeleted=false
            };


            db.Users.Add(us);
            db.SaveChanges();
            log.Info("User: " + us.ToString());

            int id = us.IDUser;
            return id;
        }

        public void Update(User user)
        {
            if (user == null)
            {
                throw new ArgumentNullException("User is null");
            }
            user.Username = RandomString(8);
            user.Email = RandomString(8) + "@" + RandomString(5) + "." + RandomString(3);
            user.Pass = ConvertToHash(RandomString(10));
            user.IsDeleted = true;
            User u = db.Users.Where(
              x => x.IDUser == user.IDUser).SingleOrDefault();
            if (u != null)
            {
                db.Entry(u).CurrentValues.SetValues(user);
                db.SaveChanges();
            }
        }

        private byte[] ConvertToHash(string pass)
        {
            using (SHA512 sha512Hash = SHA512.Create())
            {
                //From String to byte array
                byte[] bytes = sha512Hash.ComputeHash(Encoding.UTF8.GetBytes(pass));
                string hash = BitConverter.ToString(bytes).Replace("-", String.Empty);

                Console.WriteLine("The SHA512 hash of " + pass + " is: " + hash);
                return bytes;
            }

        }

        public static string RandomString(int length)
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            return new string(Enumerable.Repeat(chars, length)
                .Select(s => s[random.Next(s.Length)]).ToArray());
        }

        public User GetUser(int id)
        {
            var user = db.Users.Find(id);
            if (user == null)
            {
                throw new KeyNotFoundException("User not found");
            }
            return user;

        }
}
}