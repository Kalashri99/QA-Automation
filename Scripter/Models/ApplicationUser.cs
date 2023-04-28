using Microsoft.AspNetCore.Identity;

namespace Scripter.Models
{
    public class ApplicationUser:IdentityUser
    {
        public ApplicationUser()
        {
            this.UploadFiles = new HashSet<Uploadfile>();
            this.ExcelFiles = new HashSet<UploadExcelFile>();
        }
        public string Name { get; set; }
        public DateTime Addedon { get; set; }
        public virtual ICollection<Uploadfile> UploadFiles { get; set; }
        public virtual ICollection<UploadExcelFile> ExcelFiles { get; set; }
    }
}
