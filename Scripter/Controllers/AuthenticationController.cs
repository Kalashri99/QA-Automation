using Scripter.DbContexts;
using Scripter.Models;
using Scripter.Models.Dto;
using Scripter.Repository.IRepository;
using Scripter.Services.TokenGenerators;
using Scripter.Services.TokenValidators;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;
using Microsoft.Office.Interop.Word;

namespace Scripter.Controllers
{
    [Route("api/[controller]")]
    //[ApiController]
    public class AuthenticationController : Controller
    {
        private readonly ApplicationDbContext _db;
        ResponseDto _response;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly SignInManager<ApplicationUser> _signInManager;
        private readonly RoleManager<IdentityRole> _roleManager;
        private readonly RefreshTokenValidator _refreshTokenValidator;
        private readonly IRefreshTokenRepository _refreshTokenRepository;
        private readonly AccessTokenGenerator _accessTokenGenerator;
        private readonly RefreshTokenGenerator _refreshTokenGenerator;
        
        public AuthenticationController(ApplicationDbContext db, UserManager<ApplicationUser> userManager,
            RoleManager<IdentityRole> roleManager, SignInManager<ApplicationUser> signInManager,
            AccessTokenGenerator accessTokenGenerator, RefreshTokenGenerator refreshTokenGenerator, 
            RefreshTokenValidator refreshTokenValidator,
            IRefreshTokenRepository refreshTokenRepository
            )
        {
            _db = db;
            this._response = new ResponseDto();
            _userManager = userManager;
            _roleManager = roleManager;
            _signInManager = signInManager;
            _accessTokenGenerator = accessTokenGenerator;
            _refreshTokenGenerator = refreshTokenGenerator;
            _refreshTokenValidator = refreshTokenValidator;
            _refreshTokenRepository = refreshTokenRepository;
        }


        //To register a user

        [HttpPost]
        [Route("Register")]
        public async Task<object> Register([FromBody] RegisterViewDto model)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    var user = new ApplicationUser
                    {
                        UserName = model.UserName,
                        PhoneNumber = model.PhoneNumber,
                        Email = model.Email,
                        Name = model.Name,
                        Addedon = DateTime.Now
                    };
                    var IsUserNamePresent = await _db.Users
                        .AnyAsync(u => u.UserName == user.UserName); //Check if username is already registered
                    var IsEmailPresent = await _db.Users.AnyAsync(u => u.Email == user.Email);// Check if Email is already registered
                    var IsPhoneRegistered = await _db.Users.AnyAsync(u => u.PhoneNumber.Equals(user.PhoneNumber)); //Check if the Phone number is already present
                    if (IsUserNamePresent)
                    {
                        _response.IsSuccess = false;
                        _response.Result = Conflict();//returns conflict if username is already present
                        _response.DisplayMessage = "UserName Already Present";
                    }
                    else if (IsEmailPresent)
                    {
                        _response.IsSuccess = false;
                        _response.Result = Conflict(); //returns conflict if email is already present
                        _response.DisplayMessage = "Email Already Present";
                    }
                    else if (IsPhoneRegistered)
                    {
                        _response.IsSuccess = false;
                        _response.Result = Conflict();//returns conflict if phone number is already registered
                        _response.DisplayMessage = "Phone Number Already Present";

                    }
                    else
                    {
                        var result = await _userManager.CreateAsync(user, model.Password);//creates a user as per the given details

                        if (result.Succeeded)
                        {
                            _response.IsSuccess = true;
                            _response.Result = Ok();//If user registeration is successful it returns OK
                            _response.DisplayMessage = "User Successfully Registered";
                        }
                    }

                }
                else
                {
                    _response.IsSuccess = false;
                }
            }
            catch (Exception ex)
            {
                _response.IsSuccess = false;
                _response.ErrorMessages = new List<string>() { ex.ToString() };
            }

            return _response;
        }


        //To Login a user

        [HttpPost]
        [Route("Login")]
        public async Task<object> Login([FromBody] LoginViewDto model)
        {

            if (!ModelState.IsValid)//check if the model state is valid
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Invalid model state";
                return _response;
            }
            else
            {
                ApplicationUser user = await _db.Users.Where(u => u.Email.Equals(model.Email)).FirstOrDefaultAsync();
                if (user == null)
                {
                    _response.IsSuccess = false;
                    _response.Result = Unauthorized();//if the user is not registered returns unauthorized
                    _response.DisplayMessage = "User Does not Exist";
                    return _response;
                }
                var isCorrectPassword = await _userManager.CheckPasswordAsync(user, model.Password);
                if (!isCorrectPassword)
                {
                    _response.IsSuccess = false;
                    _response.Result = Unauthorized();//if the given password does't match the actual password retuns unauthorized
                    _response.DisplayMessage = "Password Incorrect";
                    return _response;
                }

                var accessToken = _accessTokenGenerator.GenerateToken(user);//generate access token for the user
                string refreshToken = _refreshTokenGenerator.GenerateToken(); //generate the refresh token for the user

                RefreshToken newRefreshToken = new RefreshToken()
                {
                    Token = refreshToken,
                    UserId = user.Id,
                };
                await _refreshTokenRepository.Create(newRefreshToken);

                //if the user credentials are valid then it returns the access token and the refresh token.
                _response.Result = Ok(new AuthenticatedUserResponse() 
                {
                    AccessToken = accessToken,
                    RefreshToken = refreshToken,
                });

                _response.DisplayMessage = "Logged In successfully";
                return (_response);

            }

        }



        //To logout a logged in user.
        [HttpDelete]
        [Route("Logout")]
        [Authorize(AuthenticationSchemes = "Bearer")]
        public async Task<object> Logout()
        {

            var userId = User.FindFirstValue("id"); //gives the useriD of the logged in User


            if (userId == null)
            {
                _response.IsSuccess = false;
                _response.DisplayMessage = "first login in to access";
                _response.Result = Unauthorized();
                return _response;
            }
            await _refreshTokenRepository.DeleteAll(userId);//deletes the refresh tokens of the user to logout

            _response.IsSuccess = true;
            _response.DisplayMessage = "Logged out Successfully";
            _response.Result = NoContent();

            return _response;
        }

    }
}
