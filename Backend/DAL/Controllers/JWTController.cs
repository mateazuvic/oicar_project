using System;
using System.Collections.Generic;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using System.Web.Http;

namespace DAL.Controllers
{
    public class JWTController : ApiController
    {
        // GET: JWT
        [HttpPost]
       // [Route("api/jwt/gettoken/{userId}/{username}")]
        public object GetToken(User u)
        {
            string key = "my_secret_key_12345"; //Secret key which will be used later during validation    
            var issuer = "https://localhost:44377/";  //normally this will be your site URL    

            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

           

            //Create a List of Claims, Keep claims name short    
            var permClaims = new List<Claim>();
            permClaims.Add(new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()));
            permClaims.Add(new Claim("userid", u.IDUser.ToString()));
            permClaims.Add(new Claim("username", u.Username));
            

            //Create Security Token object by giving required parameters    
            var token = new JwtSecurityToken(issuer, //Issure    
                            issuer,  //Audience    
                            permClaims,
                            expires: DateTime.Now.AddDays(1),
                            signingCredentials: credentials);
            var jwt_token = new JwtSecurityTokenHandler().WriteToken(token);
            return Ok(jwt_token);
        }

       
    }


}