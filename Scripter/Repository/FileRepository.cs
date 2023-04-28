using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Scripter.DbContexts;
using Scripter.Models;
using Scripter.Repository.IRepository;
using word = Microsoft.Office.Interop.Word;
using excel = Microsoft.Office.Interop.Excel;
using Microsoft.Office.Interop.Excel;
using Microsoft.Office.Interop.Word;
using System.Text.RegularExpressions;
using Task = System.Threading.Tasks.Task;
using System.Data;
using System.Runtime.InteropServices;
using System.Diagnostics;

namespace Scripter.Repository
{
    public class FileRepository : IFileRepository
    {
        private readonly ApplicationDbContext _db;
        private readonly UserManager<ApplicationUser> _userManager;
        public FileRepository(ApplicationDbContext db, UserManager<ApplicationUser> userManager)
        {
            _db = db;
            _userManager = userManager;
        }

        //Add uploaded word file to the database
        public async Task<object> PostFileAsync(IFormFile fileData, string fileType, string userId, int fileFormat)
        {
            try
            {
                var fileDetails = new Uploadfile()
                {
                    FileName = fileData.FileName,
                    FileType = fileType,
                    FileFormat = fileFormat
                };
                using (var stream = new MemoryStream())
                {
                    fileData.CopyTo(stream);
                    fileDetails.FileData = stream.ToArray();
                }
                ApplicationUser user = await _db.Users.Where(u => u.Id.Equals(userId)).Include(f => f.UploadFiles).FirstOrDefaultAsync();
                user.UploadFiles.Add(fileDetails);
                await _db.SaveChangesAsync();
                return fileDetails;
            } catch (Exception ex)
            {
                return ex;
            }
        }


        //Add generated excel file to the database
        public async Task PostExcelFileAsync(byte[] fileData, string fileName, string fileType, string userId)
        {
            try
            {
                var fileDetails = new UploadExcelFile()
                {
                    FileName = fileName,
                    FileType = fileType,
                    FileData = fileData,
                };
                ApplicationUser user = await _db.Users.Where(u => u.Id.Equals(userId)).Include(f => f.ExcelFiles).FirstOrDefaultAsync();
                user.ExcelFiles.Add(fileDetails);
                await _db.SaveChangesAsync();
                return;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return;
            }
        }


        //download word file by Id
        public async System.Threading.Tasks.Task<object> DownloadFileById(Guid Id,string userId)
        {
            try
            {
                var file = await _db.UploadFiles.Where(f => f.Id.Equals(Id)).FirstOrDefaultAsync();
                var content = new MemoryStream(file.FileData);
                var fileName = file.FileName;
                var path = Path.Combine(
                   Directory.GetCurrentDirectory(), "FileDownloaded",
                   file.FileName);
                CopyStream(content, path);
                var fileFormat = file.FileFormat;
                if(fileFormat == 1)
                {
                    await ToExcelAsync(fileName, userId);
                }
                else if(fileFormat == 2)
                {
                    await ExcelByPythonLogic(fileName, userId);
                }
                else if(fileFormat == 3)
                {
                    await ToExcelcov(fileName, userId); 
                }
                else if (fileFormat == 4)
                {
                    await ExceltoExcelByPython(fileName, userId);


                }
                return file;
            }
            catch (Exception)
            {
                throw;
            }
        }


        //download the excel file 
       
        public async Task<UploadExcelFile> DownloadExcelFileById(Guid Id)
        {
            try
            {
                var file = await _db.UploadExcelFiles.Where(f => f.Id.Equals(Id)).FirstOrDefaultAsync();
                byte[] excelData = file.FileData;

                // Read the byte array from the database

                // Create a temporary file to store the Excel data
                string tempFilePath = Path.GetTempFileName();
                File.WriteAllBytes(tempFilePath, excelData);

                // Create a new Excel application and workbook
                excel.Application excel = new excel.Application();
                Workbook workbook = excel.Workbooks.Open(tempFilePath);

                // Save the workbook to a file in the filesystem
                var folderPath = @"D:\RGI Project Documents\complete_project\Scripter\Org\Script\";

                var fname1 = file.FileName;
                var fname2 = file.FileName+".xlsx";
                string filePath = Path.Combine(folderPath, fname2);
                workbook.SaveAs(filePath);

                // Close the workbook and Excel application
                workbook.Close();
                excel.Quit();

                // Release the COM objects
                Marshal.ReleaseComObject(workbook);
                Marshal.ReleaseComObject(excel);


                // Delete the temporary file
                File.Delete(tempFilePath);

             
                var fileName = file.FileName+".xlsx";
                
                //Update the Master.xlsx file
                ChangeMaster(fname1);
                
                return file;
            }
            catch (Exception)
            {
                throw;
            }
        }


        //Copying the data to a file in the File system
        public void CopyStream(Stream stream, string downloadPath)
        {
            using (var fileStream = new FileStream(downloadPath, FileMode.Create, FileAccess.Write))
            {
                stream.CopyTo(fileStream);
                fileStream.Dispose();
                fileStream.Close();
            }

        }


        public void ChangeMaster(string fileName)
        {
            
            var path = @"D:\RGI Project Documents\complete_project\Scripter\Org\Script\Master.xlsx";
           

            // Open the Excel file
            excel.Application excel = new excel.Application();
            Workbook workbook = excel.Workbooks.Open(path);

            // Get the first worksheet
            Worksheet worksheet = (Worksheet)workbook.Worksheets[1];

            // Get the used range of the worksheet
            excel.Range usedRange = worksheet.UsedRange;

            // Get the row count of the used range
            int rowCount = usedRange.Rows.Count;

            // Clear the content of all rows except the first row
            if (rowCount > 1)
            {
                excel.Range contentRange = worksheet.Range["A2", "Z" + rowCount];
                contentRange.Clear();
            }

            workbook.Save();

            int rowCount2 = usedRange.Rows.Count;


            
            // Define the data to be written to the worksheet
            string[,] data = new string[,]
            {
                {fileName, "Yes", "No", "", "web"}
            };

            // Determine the starting row for writing data
            int startRow = rowCount2 + 1;

            // Determine the ending row for writing data
            int endRow = startRow + data.GetLength(0) - 1;

            // Determine the range for writing data
            excel.Range writeRange = worksheet.Range["A" + startRow, "E" + endRow];

            // Write the data to the worksheet
            writeRange.Value = data;


            // Save and close the workbook
            workbook.Save();
            workbook.Close();

            // Release the Excel object from memory
            System.Runtime.InteropServices.Marshal.ReleaseComObject(excel);


        }
        
        //Program Logic of converting word document to excel document.
        public async Task<object> ToExcelAsync(string fileName,string userId)
        {
            //Reading and opening the word file
            word.Application wordFile = new word.Application();
            var path = Path.Combine(
                   Directory.GetCurrentDirectory(), "FileDownloaded",
                   fileName);
            word.Document doc = wordFile.Documents.Open(path);
            var wordData = doc.Content.Text;

            //Creating an excel file
            excel.Application excelFile = new();
            Workbook workbook = excelFile.Workbooks.Add();
            Worksheet ws = (Worksheet)workbook.ActiveSheet;

            //Adding the headers to the excel file
            excel.Range cellRange = ws.Range["A1:G1"];
            string[] headers = new[] { "Test Case Name", "Steps", "Fields", "Type", "Data", "Locator Type", "Locator Value" };
            cellRange.set_Value(XlRangeValueDataType.xlRangeValueDefault, headers);


            //Regex pattern for extracting data from the paragraphs
            string paraPattern = @"\.\s";

            string stepsPattern = @"(?m)(?<=\bSteps:\s).*?(?=\,|\.)";
            string fieldsPattern = @"(?m)(?<=\bFields:\s).*?(?=\,|\.)";
            string typePattern = @"(?m)(?<=\bType:\s).*?(?=\,|\.)";

            string locatorTypePattern = @"(?m)(?<=\bLocator Type:\s).*?(?=\,|\.)";
            string locatorValuePattern = @"(?m)(?<=\bLocator Value:\s).*?(?=\,|\.)";

            string dataPattern = @"(?m)(?<=Data:\s).*?((?=\#)|(?=\,))";
            

            string testCaseNamePattern = @"(?<=Test Case Name:\s).*?(?=\.)";

            int lineNum = 2;

            //Iterating over each paragraph of the doucment
            foreach (Paragraph p in doc.Paragraphs)
            {

                var text = p.Range.Text;
                var sentences = Regex.Split(text, paraPattern);
                var testCaseName = "";
                var testCaseMatch = Regex.Match(sentences[0] + ".", testCaseNamePattern);
                if (testCaseMatch.Success)
                {
                    testCaseName = testCaseMatch.Groups[0].Value;

                }
                else
                {
                    Console.WriteLine("error");
                }


                var r = "A" + lineNum + ":" + "A" + lineNum;

                ws.Cells[lineNum, 1] = testCaseName;



                //Iterating over each sentence of the Paragraph
                int len = sentences.Length;
                for (int i = 1; i < len - 1; i++)
                {
                    var Steps = "";
                    var Fields = "";
                    var Type = "";
                    var Data = "";
                    var LocatorType = "";
                    var LocatorValue = "";
                    var s = sentences[i].ToString();
                    s = s + ".";
                    
                    //Extracting every field with the help of regex and storing it.
                    var stepMatch = Regex.Match(s, stepsPattern);
                    var fieldsMatch = Regex.Match(s, fieldsPattern);
                    var typeMatch = Regex.Match(s, typePattern);
                    var dataMatch = Regex.Match(s, dataPattern);
                    var locatorTypeMatch = Regex.Match(s, locatorTypePattern);
                    var locatorValueMatch = Regex.Match(s, locatorValuePattern);

                    if (stepMatch.Success)
                    {
                        Steps = stepMatch.Groups[0].Value;
                        Steps = Steps.Trim();
                    }
                    if (fieldsMatch.Success)
                    {
                        Fields = fieldsMatch.Groups[0].Value;
                        Fields = Fields.Trim();
                    }
                    if (typeMatch.Success)
                    {
                        Type = typeMatch.Groups[0].Value;
                        Type = Type.Trim();
                    }
                    if (dataMatch.Success)
                    {
                        Data = dataMatch.Groups[0].Value;
                        Data = Data.Trim();
                    }
                    if (locatorTypeMatch.Success)
                    {
                        LocatorType = locatorTypeMatch.Groups[0].Value;
                        LocatorType = LocatorType.Trim();
                    }
                    if (locatorValueMatch.Success)
                    {
                        LocatorValue = locatorValueMatch.Groups[0].Value;
                        Console.WriteLine();
                        Console.WriteLine(LocatorValue);
                        Console.WriteLine();
                        LocatorValue = LocatorValue.Trim();
                    }
                    //Adding the extracted value to the excel file.
                    string[] values = { Steps, Fields, Type, Data, LocatorType, LocatorValue };
                    string st = "B" + lineNum;
                    string end = "G" + lineNum;
                    var rg = st + ":" + end;
                    excel.Range dataRange = ws.Range[rg];
                    dataRange.set_Value(XlRangeValueDataType.xlRangeValueDefault, values);

                    lineNum = lineNum + 1;
                }
                Console.WriteLine();
            }
            ws.Columns.AutoFit();
            var n = fileName.Substring(0, fileName.Length - 5);
            var newFileName = n + ".xlsx";
            var excelpath = Path.Combine(
                   Directory.GetCurrentDirectory(), "ExcelDownLoad",
                   n);
            workbook.SaveAs(excelpath);

            workbook.Close();
            doc.Close();
            FileInfo file = new FileInfo(path);
            if (file.Exists)
            {
                file.Delete();
            }
            
            await AddExcelToDBAsync(n, userId);
                return true;
            
        }

        public async Task AddExcelToDBAsync(string fileName,string userId)
        {
            var fileName2 = @"D:\RGI Project Documents\complete_project\Scripter\ExcelDownLoad\" + fileName+".xlsx";
            byte[] fileContent = null;
            System.IO.FileStream fs = new System.IO.FileStream(fileName2, System.IO.FileMode.Open, System.IO.FileAccess.Read);
            System.IO.BinaryReader binaryReader = new System.IO.BinaryReader(fs);
            long byteLength = new System.IO.FileInfo(fileName2).Length;
            fileContent = binaryReader.ReadBytes((Int32)byteLength);
            fs.Close();
            fs.Dispose();
            binaryReader.Close();
            string fileType = ".xlsx";
            await PostExcelFileAsync(fileContent, fileName, fileType, userId);
            if (File.Exists(fileName2))
            {
                File.Delete(fileName2);
            }
            return ;

        }


        //get all word files of a user
        public async Task<object> GetAllFilesOfUser(string userId)
        {
            //fetch the user from the database along with the word file associated with the user.
            var user = await _db.Users.Include(u => u.UploadFiles).Where(p => p.Id.Equals(userId)).FirstOrDefaultAsync();

            if (user == null)
            {
                return null;//if user not found return null
            }

            var files = user.UploadFiles;
            List<Uploadfile> result = new List<Uploadfile>();
            foreach (var file in files)
            {
                result.Add(file);
            }
            return result;//returning the files of the user

        }

        //Fetch all the excel file of the user from the database
        public async Task<object> GetAllExcelFilesOfUsers(string userId)
        {
            //get the user with the fiven userId along with the excel files
            var user = await _db.Users.Include(u => u.ExcelFiles).Where(p => p.Id.Equals(userId)).FirstOrDefaultAsync();

            if (user == null)
            {
                return null;
            }
            var files = user.ExcelFiles;
            List<UploadExcelFile> result = new List<UploadExcelFile>();
            foreach (var file in files)
            {
                if(file.FileType == ".xlsx")
                {
                    result.Add(file);
                }
                
            }
            return result;

        }

        //Adding report to the database
        public async Task<object> PostReportAsync(Guid excelId)
        {
            try
            {
                //fetching the report file from the file system
                string folderPath = @"D:\RGI Project Documents\complete_project\Scripter\Report\";

                string[] htmlFiles = Directory.GetFiles(folderPath, "*.html");

                if (htmlFiles.Length > 0)
                {
                    string firstHtmlFile = htmlFiles[0];
                    byte[] fileBytes = File.ReadAllBytes(firstHtmlFile);
                    string fileName = Path.GetFileName(firstHtmlFile);

                    var fileDetails = new Report()
                    {
                        ReportName = fileName,
                        ExcelId = excelId,
                        Content = fileBytes
                    };
                    //adding report to the db
                    var report = await _db.Reports.AddAsync(fileDetails);
                    await _db.SaveChangesAsync();

                    //delete the report from the file system
                    foreach (string file in htmlFiles)
                    {
                        File.Delete(file);
                    }
                    
                }
                return true;
            }
            catch (Exception ex)
            {
                return ex;
            }


        }

        //Get all the reports of a user
        public async Task<object> GetAllReportsOfUsers(string userId)
        {
            //fetching the user from the database along along with the excel files associated with the user 
            ApplicationUser user = await _db.Users.Where(u => u.Id.Equals(userId)).Include(f => f.ExcelFiles).FirstOrDefaultAsync();
            if (user == null)
            {
                return null;
            }
            var excelFiles = user.ExcelFiles;
            
            //fetching the reports of the users 
            List<Report> result = new List<Report>();
            foreach (var excel in excelFiles)
            {
                var excelid = excel.Id;
             
                var report = await _db.Reports.Where(xl => xl.ExcelId.Equals(excelid)).FirstOrDefaultAsync();
                if(report != null)
                {
                    result.Add(report);
                }
            }
            return result;
        }

        //fetching a particular report from the database based on the report Id.
        public async Task<Report> DownloadReportById(Guid id)
        {
            var report = await _db.Reports.Where(x => x.Id.Equals(id)).FirstOrDefaultAsync();
            if(report == null)
            {
                return null;
            }
            return report;
        }


        //Program Logic of converting excel document to excel document.
        public async Task<object> ToExcelcov(string fileName, string userId)
        {
            var path = Path.Combine(
                   Directory.GetCurrentDirectory(), "FileDownloaded",
                   fileName);
            var texts = new List<string>();
            //Creating an excel file
            excel.Application excelFile = new();
            Workbook workbook = excelFile.Workbooks.Add();
            Worksheet ws = (Worksheet)workbook.ActiveSheet;

            // Specify the Excel and Word file paths
            /* string excelFilePath = @"D:\Format2(1).xlsx";
             string DaisyExcelFilePath = @"D:\DaisyInput.xlsx";

             if (File.Exists(DaisyExcelFilePath))
                 File.Delete(DaisyExcelFilePath);*/

            // Create an Excel application object
            var excelApp = new excel.Application();

            // Open the Excel file
            var excelWorkbook = excelApp.Workbooks.Open(path);

            // Get the first worksheet in the Excel file
            var excelWorksheet = excelWorkbook.Worksheets[1];


            // Get the range of cells in the Excel worksheet
            var excelRange = excelWorksheet.UsedRange;

            var daisyDoc = excelApp.Workbooks.Add();
            var daisyWorksheet = daisyDoc.Worksheets[1];


            //-----------logic for converting excel to excel-------------- 
            excel.Range cellRange = daisyWorksheet.Range["A1:G1"];
            string[] headers = new[] { "Test Case Name", "Steps", "Fields", "Type", "Data", "Locator Type", "Locator Value" };
            cellRange.set_Value(XlRangeValueDataType.xlRangeValueDefault, headers);
            //Regex pattern for extracting data from the 2nd cell
            string paraPattern = @"\.\s";

            string stepsPattern = @"(?m)(?<=\bSteps:\s).*?(?=\,|\.)";
            string fieldsPattern = @"(?m)(?<=\bFields:\s).*?(?=\,|\.)";
            string typePattern = @"(?m)(?<=\bType:\s).*?(?=\,|\.)";

            string locatorTypePattern = @"(?m)(?<=\bLocator Type:\s).*?(?=\,|\.)";
            string locatorValuePattern = @"(?m)(?<=\bLocator Value:\s).*?(?=\,|\.)";

            string dataPattern = @"(?m)(?<=Data:\s).*?((?=\#)|(?=\,))";


            string testCaseNamePattern = @"(?<=Test Case Name:\s).*?(?=\.)";

            int lineNum = 2;

            for (int j = 2; j <= excelRange.Rows.Count; ++j)
            {
                var text = excelRange.Cells[j, 2].Value;

                var sentences = Regex.Split(text, paraPattern);
                var testCaseName = "";
                var testCaseMatch = Regex.Match(sentences[0] + ".", testCaseNamePattern);
                if (testCaseMatch.Success)
                {
                    testCaseName = testCaseMatch.Groups[0].Value;

                }
                else
                {
                    Console.WriteLine("error");
                }




                var r = "A" + lineNum + ":" + "A" + lineNum;

                daisyWorksheet.Cells[lineNum, 1] = testCaseName;



                //Iterating over each sentence of the Paragraph
                int len = sentences.Length;
                for (int i = 1; i < len; i++)
                {
                    var Steps = "";
                    var Fields = "";
                    var Type = "";
                    var Data = "";
                    var LocatorType = "";
                    var LocatorValue = "";
                    var s = sentences[i].ToString();
                    s = s + ".";

                    //Extracting every field with the help of regex and storing it.
                    var stepMatch = Regex.Match(s, stepsPattern);
                    var fieldsMatch = Regex.Match(s, fieldsPattern);
                    var typeMatch = Regex.Match(s, typePattern);
                    var dataMatch = Regex.Match(s, dataPattern);
                    var locatorTypeMatch = Regex.Match(s, locatorTypePattern);
                    var locatorValueMatch = Regex.Match(s, locatorValuePattern);

                    if (stepMatch.Success)
                    {
                        Steps = stepMatch.Groups[0].Value;
                        Steps = Steps.Trim();
                    }
                    if (fieldsMatch.Success)
                    {
                        Fields = fieldsMatch.Groups[0].Value;
                        Fields = Fields.Trim();
                    }
                    if (typeMatch.Success)
                    {
                        Type = typeMatch.Groups[0].Value;
                        Type = Type.Trim();
                    }
                    if (dataMatch.Success)
                    {
                        Data = dataMatch.Groups[0].Value;
                        Data = Data.Trim();
                    }
                    if (locatorTypeMatch.Success)
                    {
                        LocatorType = locatorTypeMatch.Groups[0].Value;
                        LocatorType = LocatorType.Trim();
                    }
                    if (locatorValueMatch.Success)
                    {
                        LocatorValue = locatorValueMatch.Groups[0].Value;
                        Console.WriteLine();
                        //  Console.WriteLine(LocatorValue);
                        Console.WriteLine();
                        LocatorValue = LocatorValue.Trim();
                    }
                    //Adding the extracted value to the excel file.
                    string[] values = { Steps, Fields, Type, Data, LocatorType, LocatorValue };
                    string st = "B" + lineNum;
                    string end = "G" + lineNum;
                    var rg = st + ":" + end;
                    excel.Range dataRange = daisyWorksheet.Range[rg];
                    dataRange.set_Value(XlRangeValueDataType.xlRangeValueDefault, values);

                    lineNum = lineNum + 1;
                }
                Console.WriteLine();
            }

            ws.Columns.AutoFit();
            var n = fileName.Substring(0, fileName.Length - 5);
            var newFileName = n + ".xlsx";
            var excelpath = Path.Combine(
                   Directory.GetCurrentDirectory(), "ExcelDownLoad",
                   n);
            excelWorkbook.Close();
            daisyDoc.SaveAs(excelpath);

            daisyDoc.Close();

            excelApp.Quit();
            FileInfo file = new FileInfo(path);
            if (file.Exists)
            {
                file.Delete();
            }

            await AddExcelToDBAsync(n, userId);
            return true;

        }

        public async Task<object> ExcelByPythonLogic(string fileName, string userId)
        {
            string pythonExePath = @"D:\RGI Project Documents\complete_project\Scripter\Program Logic\LatestLogic.exe"; // Path to Python executable
            //string scriptPath = @"C: \Users\kalashri_patil\Downloads\AutoTesterSprint1Final - master\AutoTesterSprint1Final - master\Program Logic\Main_File.py"; // Path to Python script
            //string filepath = @"C:\Users\kalashri_patil\Downloads\AutoTesterSprint1Final-master\AutoTesterSprint1Final-master\Sprint UD Doc.docx"; // Input to be passed to the script

            var filepath = Path.Combine(
                   Directory.GetCurrentDirectory(), "FileDownloaded",
                   fileName);
            var n = fileName.Substring(0, fileName.Length - 5);
            var newFileName = n + ".xlsx";
            var excelpath = Path.Combine(
                   Directory.GetCurrentDirectory(), "ExcelDownLoad",
                   newFileName);

            ProcessStartInfo start = new ProcessStartInfo();
            start.FileName = pythonExePath;
            start.Arguments = $"\"{filepath}\" \"{excelpath}\"";
            start.UseShellExecute = false;
            start.RedirectStandardOutput = true;
            
            Process process = Process.Start(start);
            while (!process.HasExited && process.Responding)
            {
                // Wait for a short period of time before checking again
                System.Threading.Thread.Sleep(1000); // 500 milliseconds
            }

            if (!process.Responding || process.HasExited)
            {
                process.Kill();
             
            }

            //using (Process process = Process.Start(start))
            //{
            //    using (StreamReader reader = process.StandardOutput)
            //    {
            //        string result = reader.ReadToEnd();
            //        return Ok(result);
            //    }
            //}


            await AddExcelToDBAsync(n, userId);
            return true;

        }
        //ExceltoExcelByPython





        public async Task<object> ExceltoExcelByPython(string fileName, string userId)
        {
            string pythonExePath = @"D:\RGI Project Documents\complete_project\Scripter\Program Logic\Excel_to_Excel.exe"; // Path to Python executable
                                                                                       //string scriptPath = @"C: \Users\kalashri_patil\Downloads\AutoTesterSprint1Final - master\AutoTesterSprint1Final - master\Program Logic\Main_File.py"; // Path to Python script
                                                                                       //string filepath = @"C:\Users\kalashri_patil\Downloads\AutoTesterSprint1Final-master\AutoTesterSprint1Final-master\Sprint UD Doc.docx"; // Input to be passed to the script



            var filepath = Path.Combine(Directory.GetCurrentDirectory(), "FileDownloaded", fileName);
            var n = fileName.Substring(0, fileName.Length - 5);
            var newFileName = n + ".xlsx";
            var excelpath = Path.Combine(
            Directory.GetCurrentDirectory(), "ExcelDownLoad",
            newFileName);





            ProcessStartInfo start = new ProcessStartInfo();
            start.FileName = pythonExePath;
            start.Arguments = $"\"{filepath}\" \"{excelpath}\"";
            start.UseShellExecute = false;
            start.RedirectStandardOutput = true;





            Process process = Process.Start(start);
            while (!process.HasExited && process.Responding)
            {
                // Wait for a short period of time before checking again
                System.Threading.Thread.Sleep(1000); // 500 milliseconds
            }





            if (!process.Responding || process.HasExited)
            {
                process.Kill();





            }





            //using (Process process = Process.Start(start))
            //{
            //    using (StreamReader reader = process.StandardOutput)
            //    {
            //        string result = reader.ReadToEnd();
            //        return Ok(result);
            //    }
            //}






            await AddExcelToDBAsync(n, userId);
            return true;





        }



    }
} 
