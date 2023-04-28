using Scripter.Models;

namespace Scripter.Repository.IRepository
{
    public interface IFileRepository
    {
        public Task<object> PostFileAsync(IFormFile fileData, string fileType, string userId,int fileFormat);
        public Task<object> PostReportAsync(Guid excelId);
        public Task<object> DownloadFileById(Guid id,string userId);
        public Task<UploadExcelFile> DownloadExcelFileById(Guid id);
        public Task<Report> DownloadReportById(Guid id);
        public Task<object> GetAllFilesOfUser(string userId);
        public Task<object> GetAllReportsOfUsers(string userId);
        public Task<object> GetAllExcelFilesOfUsers(string userId);
    }
}
