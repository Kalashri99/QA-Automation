using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Scripter.Models
{
    public class Report
    {
        [Key]
        public Guid Id { get; set; }
        public string ReportName { get; set; }
        public byte[] Content { get; set; }

        public Guid ExcelId { get; set; }
        [ForeignKey("ExcelId")]
        public virtual UploadExcelFile UploadExcelFiles { get; set; }
    }
}
