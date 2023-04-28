using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Scripter.Migrations
{
    public partial class addedFileFormatInUploadFile : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "FileFormat",
                table: "UploadFiles",
                type: "int",
                nullable: false,
                defaultValue: 0);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "FileFormat",
                table: "UploadFiles");
        }
    }
}
