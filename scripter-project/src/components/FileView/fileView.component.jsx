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

export default function FileView(props) {
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

  useEffect(() => {
    const getData = async () => {
      await axios({
        method: "get",
        url: "https://localhost:7259/api/File/GetFilesByUser",
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
    console.log(data);
    return (
      <tr>
        <td>
          <div className="d-flex align-items-center">
            <div className="ms-3">
              <p className="fw-bold mb-1">{data.fileName}</p>
            </div>
          </div>
        </td>
        <td>
          <p className="fw-normal mb-1">{data.fileType}</p>
        </td>

        <td>
          <MDBBtn
            color="link"
            rounded
            size="sm"
            onClick={() => {
              navigate("/Loading", {
                state: { fileId: data.id },
              });
            }}
          >
            Convert
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
          <th scope="col">File Type</th>
          <th scope="col">Actions</th>
        </tr>
      </MDBTableHead>
      <MDBTableBody>{files}</MDBTableBody>
    </MDBTable>
  );
}
