using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Scripter.Models;
using Scripter.Models.Dto;
using Scripter.Repository.IRepository;
using System.Diagnostics;
using System.Security.Claims;
using System.IO;
using System.Windows;


namespace Scripter.Controllers
{
    [Route("api/[controller]")]
    public class FileController : Controller
    {
        private readonly IFileRepository _fileRepository;
        ResponseDto _response;
        public FileController(IFileRepository fileRepository)
        {
            _fileRepository = fileRepository;
            this._response = new ResponseDto();
        }

        [HttpPost]
        [Route("uploadFile")]
        [Authorize(AuthenticationSchemes ="Bearer")]
        public async Task<ResponseDto> UploadFile([FromForm] FileUploadDto uploadFileDetials)
        {
            if(uploadFileDetials.FileDetails == null)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "File Details are Null";
                return _response;
            }
            try
            {
                var userId = User.FindFirstValue("id");
                var fileType = uploadFileDetials.FileType;
                var fileFormat = uploadFileDetials.FileFormat;
                var result = await _fileRepository.PostFileAsync(uploadFileDetials.FileDetails,fileType,userId,fileFormat);
                _response.Result = result;
                _response.IsSuccess = true;
                _response.DisplayMessage = "File Uploaded Successfully";
            }
            catch (Exception ex)
            {
                _response.IsSuccess=false;
                _response.ErrorMessages = new List<string>() { ex.ToString() };
            }
            return _response;
        }

        [HttpGet]
        [Route("DownloadFile")]
        [Authorize(AuthenticationSchemes = "Bearer")]
        public async Task<ResponseDto> DownloadFile(Guid Id)
        {
            if (!ModelState.IsValid)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Bad Request";
                return _response;
            }

            try
            {
                var userId = User.FindFirstValue("id");
                await _fileRepository.DownloadFileById(Id,userId);
                _response.IsSuccess = true;
                _response.Result = Ok();
                _response.DisplayMessage = "File SuccessFully Download"; 
            }catch (Exception ex)
            {
                _response.IsSuccess=false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Error";
                _response.ErrorMessages = new List<string>() { ex.ToString() };
                //_response.ErrorMessages.Add(ex.ToString());
            }

            return _response;

        }



        [HttpGet]
        [Route("GetExcelFilesByUser")]
        [Authorize(AuthenticationSchemes ="Bearer")]
        public async Task<ResponseDto> GetExcelFilesByUser()
        {
            
            if (!ModelState.IsValid)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Bad Request";
                return _response;
            }

            try
            {
                var user = User.FindFirstValue("Id");
                if (user == null)
                {
                    _response.IsSuccess = false;
                    _response.Result = BadRequest();
                    _response.DisplayMessage = "User not found";
                }
                var result = await _fileRepository.GetAllExcelFilesOfUsers(user);
                _response.IsSuccess = true;
                _response.Result = result;
                _response.DisplayMessage = "File SuccessFully fethched";
            }
            catch (Exception ex)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Error";
                _response.ErrorMessages = new List<string>() { ex.ToString() };
                //_response.ErrorMessages.Add(ex.ToString());
            }
            return _response;
        }

        [HttpGet]
        [Route("GetFilesByUser")]
        [Authorize(AuthenticationSchemes = "Bearer")]
        public async Task<ResponseDto> GetFilesByUser()
        {

            if (!ModelState.IsValid)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Bad Request";
                return _response;
            }

            try
            {
                var user = User.FindFirstValue("Id");
                if (user == null)
                {
                    _response.IsSuccess = false;
                    _response.Result = BadRequest();
                    _response.DisplayMessage = "User not found";
                }
                var result = await _fileRepository.GetAllFilesOfUser(user);
                _response.IsSuccess = true;
                _response.Result = result;
                _response.DisplayMessage = "File SuccessFully fethched";
            }
            catch (Exception ex)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Error";
                _response.ErrorMessages = new List<string>() { ex.ToString() };
                //_response.ErrorMessages.Add(ex.ToString());
            }

            return _response;

        }


        //private static void Process_OutputDataReceived(object sender, DataReceivedEventArgs e)
        //{
        //    if (e.Data != null && e.Data.Contains("Press any key to continue"))
        //    {
        //        Thread.Sleep(1000); // Wait for the message to fully display
        //        SendKeys.SendWait("{ENTER}");
        //    }
        //    Console.WriteLine(e.Data); // Output the received data
        //}

        [HttpGet]
        [Route("Execute")]
        [Authorize(AuthenticationSchemes = "Bearer")]
        public async Task<ResponseDto> ExecuteDaisy(Guid Id)
        {
            if (!ModelState.IsValid)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Bad Request";
                return _response;
            }

            try
            {
                var result = await _fileRepository.DownloadExcelFileById(Id);
                var fileName = result.FileName;

                if (result != null)
                {
//               Console.WriteLine(result);
                    Process process = new Process();
                    process.StartInfo.FileName = @"D:\RGI Project Documents\complete_project\Scripter\RunDaisy.bat";

                    process.StartInfo.RedirectStandardInput = true;
                    process.StartInfo.RedirectStandardOutput = true;
                    process.StartInfo.UseShellExecute = false;


                 process.Start();
                    // Send the "exit" command to the process to exit it
                    process.StandardInput.WriteLine("exit");



                    // Read the output from the console application
                    string output = process.StandardOutput.ReadToEnd();



                    // Wait for the process to exit
                    process.WaitForExit();
                    //process.BeginOutputReadLine();
                    // process.WaitForExit();

                    //Console.ReadLine(); // Keeps console window open so you can see the output





                    // Wait for the process to exit or become responsive again
                    while (!process.HasExited && process.Responding)
                    {
                        // Wait for a short period of time before checking again
                        System.Threading.Thread.Sleep(1000); // 500 milliseconds
                    }

                    
                    if (!process.Responding || process.HasExited)
                    {

                        process.Kill();
                        var res = await _fileRepository.PostReportAsync(Id);
                        
                        string filePath = @"D:\RGI Project Documents\complete_project\Scripter\Org\Script\" + fileName + ".xlsx";
                        if (System.IO.File.Exists(filePath))
                        {
                            System.IO.File.Delete(filePath);
                        }
                        
                        if (res != null)
                        {
                            _response.IsSuccess = true;
                            _response.Result = Ok();
                            _response.DisplayMessage = "Daisy Execution Successful";
                        }

                        
                      
                    }
                    else
                    {
                        // The process has exited or is still unresponsive
                        Console.WriteLine("The process responsive.");
                    }


                }
            }
            catch (Exception ex)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Error";
                _response.ErrorMessages = new List<string>() { ex.ToString() };
                //_response.ErrorMessages.Add(ex.ToString());
            }

            return _response;

        }



        [HttpGet]
        [Route("GetReportByUser")]
        [Authorize(AuthenticationSchemes = "Bearer")]
        public async Task<ResponseDto> GetReportByUser()
        {

            if (!ModelState.IsValid)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Bad Request";
                return _response;
            }

            try
            {
                var user = User.FindFirstValue("Id");
                if (user == null)
                {
                    _response.IsSuccess = false;
                    _response.Result = BadRequest();
                    _response.DisplayMessage = "User not found";
                }
                var result = await _fileRepository.GetAllReportsOfUsers(user);
                _response.IsSuccess = true;
                _response.Result = result;
                _response.DisplayMessage = "File SuccessFully fethched";
            }
            catch (Exception ex)
            {
                _response.IsSuccess = false;
                _response.Result = BadRequest();
                _response.DisplayMessage = "Error";
                _response.ErrorMessages = new List<string>() { ex.ToString() };
                //_response.ErrorMessages.Add(ex.ToString());
            }
            return _response;
        }



        [HttpGet]
        [Route("download/{id}")]
        [Authorize(AuthenticationSchemes = "Bearer")]
        public async Task<IActionResult> downloadReport(Guid id)
        {

            if (!ModelState.IsValid)
            {
                return BadRequest();
            }

            try
            {
            
                var result = await _fileRepository.DownloadReportById(id);
                if(result != null)
                {

                    return File(result.Content, "text/html", result.ReportName+".html");
                    
                }
                
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
                //_response.ErrorMessages.Add(ex.ToString());
            }
            return BadRequest();
        }


    }
}
