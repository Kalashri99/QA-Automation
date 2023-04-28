using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Scripter.Models;

namespace Scripter.DbContexts
{
    public class ApplicationDbContext:IdentityDbContext<ApplicationUser>
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options):base(options)
        {

        }
        public DbSet<RefreshToken> RefreshTokens { get; set; }
        public DbSet<Uploadfile> UploadFiles { get; set; }
        public DbSet<UploadExcelFile> UploadExcelFiles { get; set; }
        public DbSet<Report> Reports { get; set; }
    } 
}
