using System.ComponentModel.DataAnnotations;

namespace Scripter.Models
{
    public class UploadExcelFile
    {
        [Key]
        public Guid Id { get; set; }
        public byte[] FileData { get; set; }
        public string FileType { get; set; }
        public string FileName { get; set; }
    }
}
