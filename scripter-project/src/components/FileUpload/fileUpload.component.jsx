// import React from "react";
// import { useState, useContext } from "react";
// import { MDBFile, MDBBtn, MDBContainer } from "mdb-react-ui-kit";
// import axios from "axios";

// export default function FileUpload() {
//   const [fileDetails, setFileDetails] = useState({
//     fDetails: null,
//     fileType: "",
//   });

//   const handleFile = (e) => {
//     let copiedDetails = { ...fileDetails };
//     copiedDetails.fDetails = e.target.files[0];
//     copiedDetails.fileType = ".docx";
//     setFileDetails((d) => ({
//       ...copiedDetails,
//     }));
//   };

//   const formData = new FormData();
//   formData.append("FileDetails", fileDetails.fDetails);
//   formData.append("FileType", fileDetails.fileType);
//   const handleUpload = async (e) => {
//     console.log(fileDetails);
//     e.preventDefault();
//     await axios({
//       method: "post",
//       url: "https://localhost:7259/api/File/uploadFile",
//       data: formData,
//       headers: {
//         Authorization: `bearer ${localStorage.getItem("accessToken")}`,
//         "Content-Type": "multipart/form-data",
//       },
//     })
//       .then((res) => {
//         console.log(res);
//       })
//       .catch((error) => {
//         console.log("Error");
//       });
//   };
//   return (
//     <MDBContainer>
//       <MDBFile
//         label="Choose the word file."
//         id="customFile"
//         onChange={handleFile}
//       />
//       <MDBBtn className="me-1" onClick={handleUpload}>
//         Upload
//       </MDBBtn>
//     </MDBContainer>
//   );
// }

import React from "react";
import { useState, useContext } from "react";
import { MDBFile, MDBIcon, MDBContainer } from "mdb-react-ui-kit";
import Button from "react-bootstrap/Button";
import "../FileUpload/fileUpload.css";
import axios from "axios";
export default function FileUpload() {
  const [fileDetails, setFileDetails] = useState({
    fDetails: null,
    fileType: "",
    fileFormat: "",
  });
  const [res1, setres] = useState(false);

  const handleFile = (e) => {
    let copiedDetails = { ...fileDetails };
    copiedDetails.fDetails = e.target.files[0];
    setFileDetails((d) => ({
      ...copiedDetails,
    }));
  };

  const formData = new FormData();
  formData.append("FileDetails", fileDetails.fDetails);

  const handlePostData = async () => {
    await axios({
      method: "post",
      url: "https://localhost:7259/api/File/uploadFile",
      data: formData,
      headers: {
        Authorization: `bearer ${localStorage.getItem("accessToken")}`,
        "Content-Type": "multipart/form-data",
      },
    })
      .then((res) => {
        window.location.reload();
        console.log(res);
        setres(false);
      })
      .catch((error) => {
        console.log("Error");
        setres(true);
      });
  };

  const handleUpload = async (fileFormat, fileType, e) => {
    console.log(fileDetails);
    let copiedDetails = { ...fileDetails };
    copiedDetails.fileType = fileType;
    copiedDetails.fileFormat = fileFormat;
    setFileDetails((d) => ({
      ...copiedDetails,
    }));
    console.log(fileDetails);
    formData.append("FileType", fileType);
    formData.append("FileFormat", fileFormat);
    console.log(formData);

    handlePostData();
    //e.preventDefault();
  };
  return (
    <MDBContainer>
      <MDBFile className="mr-y" id="customFile" onChange={handleFile} />
      <p align="center">
        {/* <button type="button" class="btn btn-outline-primary">Primary</button> */}
        <Button
          variant="outline-primary"
          onClick={() => {
            handleUpload(1, ".docx");
          }}
        >
          word to xlsx
        </Button>{" "}
        <Button
          variant="outline-primary"
          onClick={() => {
            handleUpload(2, ".docx");
          }}
        >
          UD to xlsx
        </Button>{" "}
        <Button
          variant="outline-primary"
          onClick={() => {
            handleUpload(3, ".xlsx");
          }}
        >
          xlsx to xlsx
        </Button>{" "}
        <Button
          variant="outline-primary"


          onClick={() => {

            handleUpload(4, ".xlsx");




          }}

        >

          xlsx to xlsxloc

        </Button>{" "}
      </p>
    </MDBContainer>
  );
}
