using System.ComponentModel.DataAnnotations;

namespace Scripter.Models.Dto
{
    public class excelUploadDto
    {
        [Key]
        public Guid Id { get; set; }
        public byte[] FileData { get; set; }
        public string FileType { get; set; }
        public string FileName { get; set; }
    }
}
