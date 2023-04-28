import React, { useEffect } from "react";
import { MDBSpinner, MDBBtn } from "mdb-react-ui-kit";
import { useLocation } from "react-router-dom";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function Loading() {
  const navigate = useNavigate();
  const location = useLocation();
  console.log(location.state.fileId);

  const fileId = location.state.fileId;
  useEffect(() => {
    const getData = async () => {
      await axios({
        method: "get",
        url: "https://localhost:7259/api/File/DownloadFile?Id=" + fileId,
        headers: {
          Authorization: `bearer ${localStorage.getItem("accessToken")}`,
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          console.log("GotTheFiles");
          console.log(res);
          if (res.data.isSuccess) {
            navigate("/ExcelDashboard");
          } else {
            navigate("/Message", {
              state: {
                heading: "Oops!!!! ",
                message: "An Error Occured please Try Again.",
              },
            });
          }
          // navigate("/Message", {
          //   state: {
          //     heading: "File Downloaded Successfully",
          //     message:
          //       "Your Excel File is ready, please find it in this location",
          //   },
          // });

          //setFileData(res.data.result);
        })
        .catch((err) => {
          navigate("/Message", {
            state: {
              heading: "Oops!!!! ",
              message: "An Error Occured please Try Again.",
            },
          });
        });
    };
    getData();
  }, [fileId, navigate]);
  return (
    <>
      <MDBBtn disabled>
        <MDBSpinner size="sm" role="status" tag="span" className="me-2" />
        convert...
      </MDBBtn>
    </>
  );
}
