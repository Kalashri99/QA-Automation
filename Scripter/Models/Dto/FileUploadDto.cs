namespace Scripter.Models.Dto
{
    public class FileUploadDto
    {
        public IFormFile FileDetails { get; set; }

        public string FileType { get; set; }

        public int FileFormat { get; set; }
    }
}
