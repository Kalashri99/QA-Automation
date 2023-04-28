/*
import React, { useEffect } from "react";

import { MDBSpinner, MDBBtn } from "mdb-react-ui-kit";



import { useLocation } from "react-router-dom";

import axios from "axios";

import { useNavigate } from "react-router-dom";



export default function Executing() {

  const navigate = useNavigate();

  const location = useLocation();

  const id = location.state.id;



  useEffect(() => {

    const handleExecute = async (id) => {

      await axios({

        method: "get",

        url: "https://localhost:7259/api/File/Execute?Id=" + id,

        headers: {

          Authorization: `bearer ${localStorage.getItem("accessToken")}`,

          "Content-Type": "application/json",

        },

      })

        .then((res) => {

          console.log(res);

          navigate("/ReportDashboard");

        })

        .catch((err) => {

          console.log(err);

        });

    };

    handleExecute();

  });



  return (

    <>

      {" "}

      <MDBBtn disabled>

        {" "}

        <MDBSpinner size="sm" role="status" tag="span" className="me-2" />

        Executing... {" "}

      </MDBBtn>

      {" "}

    </>

  );

}

import React, { useEffect } from "react";

import { MDBSpinner, MDBBtn } from "mdb-react-ui-kit";




export default function Executing() {

  return (

    <>

      <MDBBtn disabled>

        <MDBSpinner size="sm" role="status" tag="span" className="me-2" />

        Executing...

      </MDBBtn>

    </>

  );

}
*/
import React, { useEffect } from "react";

import { MDBSpinner, MDBBtn } from "mdb-react-ui-kit";

import { useLocation } from "react-router-dom";

import axios from "axios";

import { useNavigate } from "react-router-dom";




export default function Executing() {

  const navigate = useNavigate();

  const location = useLocation();

  console.log(location.state.fileId);




  const fileId = location.state.fileId;

  useEffect(() => {

    const getData = async () => {

      await axios({

        method: "get",

        url: "https://localhost:7259/api/File/Execute?Id=" + fileId,

        headers: {

          Authorization: `bearer ${localStorage.getItem("accessToken")}`,

          "Content-Type": "application/json",

        },

      })

        .then((res) => {

          console.log("FileExecuted");

          console.log(res);

          if (res.data.isSuccess) {

            navigate("/ReportDashboard");

          } else {

            navigate("/Message", {

              state: {

                heading: "Oops!!!! ",

                message: "An Error Occured please Try Again.",

              },

            });

          }

          // navigate("/Message", {

          //   state: {

          //     heading: "File Downloaded Successfully",

          //     message:

          //       "Your Excel File is ready, please find it in this location",

          //   },

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

        Executing...

      </MDBBtn>

    </>

  );

}