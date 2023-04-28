import React, { useState, useEffect } from "react";

import {
  MDBBadge,
  MDBBtn,
  MDBTable,
  MDBTableHead,
  MDBTableBody,
  MDBRadio,
} from "mdb-react-ui-kit";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function ReportView(props) {
  const navigate = useNavigate();
  const [fileData, setFileData] = useState([]);
  //const [isConvertClicked, setConvert] = useState(false);
  const [fileId, setFileId] = useState("");

  const handleClick = () => {
    //e.preventDefault();
    console.log(fileId);
    //setFileId(fil);
    OnConvertClick();
  };

  const radioChecked = (e) => {
    e.preventDefault();
    console.log(e.targer.value);
    setFileId(e.targer.value);
  };

  const OnConvertClick = async () => {
    //setConvert(true);
    console.log("FileId" + fileId);
    props.handleISConvert(true);
  };

  const downloadFile = async (fName, id) => {
    // const response = await fetch(
    //   `https://localhost:7259/api/File/download/${id}`,
    //   {
    //     headers: {
    //       Authorization: `bearer ${localStorage.getItem("accessToken")}`,
    //       "Content-Type": "application/json",
    //     },
    //   }
    // );
    // console.log(response);
    // const blob = await response.data.result.blob();
    // const url = window.URL.createObjectURL(new Blob([blob]));
    // const link = document.createElement("a");
    // link.href = url;
    // link.setAttribute(
    //   "download",
    //   response.headers.get("content-disposition").split("filename=")[1]
    // );
    // document.body.appendChild(link);
    // link.click();

    try {
      const response = await axios.get(
        `https://localhost:7259/api/File/download/${id}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
          responseType: "blob",
        }
      );
      console.log(response);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", fName + ".html");
      document.body.appendChild(link);
      link.click();
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    const getData = async () => {
      await axios({
        method: "get",
        url: "https://localhost:7259/api/File/GetReportByUser",
        headers: {
          Authorization: `bearer ${localStorage.getItem("accessToken")}`,
          "Content-Type": "application/json",
        },
      }).then((res) => {
        console.log(res);
        setFileData(res.data.result);
      });
    };
    getData();
  }, []);

  const files = fileData.map((data) => {
    var fid = data.id;
    var fName = data.reportName;
    console.log(data);
    return (
      <tr>
        <td>
          <div className="d-flex align-items-center">
            <div className="ms-3">
              <p className="fw-bold mb-1">{data.reportName}</p>
            </div>
          </div>
        </td>

        <td>
          <MDBBtn
            color="link"
            rounded
            size="sm"
            onClick={() => {
              downloadFile(fName, fid);
            }}
          >
            Download
          </MDBBtn>
        </td>
      </tr>
    );
  });

  return (
    <MDBTable align="middle">
      <MDBTableHead>
        <tr>
          <th scope="col">File Name</th>
          <th scope="col">Actions</th>
        </tr>
      </MDBTableHead>
      <MDBTableBody>{files}</MDBTableBody>
    </MDBTable>
  );
}
