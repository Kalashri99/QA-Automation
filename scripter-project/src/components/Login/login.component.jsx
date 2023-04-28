// import React from "react";
// import {
//   MDBContainer,
//   MDBCol,
//   MDBRow,
//   MDBBtn,
//   MDBIcon,
//   MDBInput,
//   MDBCheckbox,
// } from "mdb-react-ui-kit";
// import { useState, useContext } from "react";
// import axios from "axios";
// import { useNavigate } from "react-router-dom";
// import Alert from "react-bootstrap/Alert";
// import { UserContext } from "../../contexts/user.context";
// function Login() {
//   //const token = localStorage.getItem("accessToken");
//   const [loginDetials, setLoginDetails] = useState({
//     email: "",
//     password: "",
//     rememberMe: true,
//   });

//   const { currentUser, setCurrentUser } = useContext(UserContext);

//   const [status, setStatus] = useState(undefined);
//   const navigate = useNavigate();
//   const handleEmail = (e) => {
//     const copiedDetails = { ...loginDetials };
//     copiedDetails.email = e.target.value;
//     setLoginDetails((details) => ({
//       ...copiedDetails,
//     }));
//   };
//   const handlePassword = (e) => {
//     const copiedDetails = { ...loginDetials };
//     copiedDetails.password = e.target.value;
//     setLoginDetails((details) => ({
//       ...copiedDetails,
//     }));
//   };

//   const handleLogin = async (e) => {
//     e.preventDefault();
//     await axios({
//       method: "post",
//       url: "https://localhost:7259/api/Authentication/Login",
//       data: loginDetials,
//       headers: {
//         "Content-Type": "application/json",
//       },
//     })
//       .then((res) => {
//         if (res.data.isSuccess) {
//           setStatus({ type: "success" });

//           localStorage.setItem(
//             "accessToken",
//             res.data.result.value.accessToken
//           );
//           setCurrentUser(res.data.result.value.accessToken);
//           console.log("Current User start");
//           console.log(currentUser);
//           console.log("Current User end");
//           console.log(localStorage.getItem("accessToken"));

//           navigate("/Dashboard");
//         } else {
//           setStatus({ type: "error" });
//         }
//       })
//       .catch((error) => {
//         setStatus({ type: "error", error });
//       });
//   };

//   const HandleRegister = () => {
//     navigate("/");
//   };

//   return (
//     <MDBContainer fluid className="p-3 my-5 h-custom">
//       {status?.type === "success" && (
//         <Alert key={status} variant={"success"}>
//           User Successfully Registered
//         </Alert>
//       )}
//       {status?.type === "error" && (
//         <Alert key={status} variant={"danger"}>
//           User Login failed.
//         </Alert>
//       )}
//       <MDBRow>
//         <MDBCol col="10" md="6">
//           <img
//             src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.webp"
//             class="img-fluid"
//             alt="Sample"
//           />
//         </MDBCol>

//         <MDBCol col="4" md="6">
//           {/* <div className="d-flex flex-row align-items-center justify-content-center">
//             <p className="lead fw-normal mb-0 me-3">Sign in with</p>

//             <MDBBtn floating size="md" tag="a" className="me-2">
//               <MDBIcon fab icon="facebook-f" />
//             </MDBBtn>

//             <MDBBtn floating size="md" tag="a" className="me-2">
//               <MDBIcon fab icon="twitter" />
//             </MDBBtn>

//             <MDBBtn floating size="md" tag="a" className="me-2">
//               <MDBIcon fab icon="linkedin-in" />
//             </MDBBtn>
//           </div>

//           <div className="divider d-flex align-items-center my-4">
//             <p className="text-center fw-bold mx-3 mb-0">Or</p>
//           </div> */}

//           <MDBInput
//             wrapperClass="mb-4"
//             label="Email address"
//             id="email"
//             type="email"
//             size="lg"
//             onChange={handleEmail}
//           />
//           <MDBInput
//             wrapperClass="mb-4"
//             label="Password"
//             id="password"
//             type="password"
//             size="lg"
//             onChange={handlePassword}
//           />

//           <div className="d-flex justify-content-between mb-4">
//             <MDBCheckbox
//               name="flexCheck"
//               value=""
//               id="flexCheckDefault"
//               label="Remember me"
//             />
//             <a href="!#">Forgot password?</a>
//           </div>

//           <div className="text-center text-md-start mt-4 pt-2">
//             <MDBBtn className="mb-0 px-5" size="lg" onClick={handleLogin}>
//               Login
//             </MDBBtn>
//             <p className="small fw-bold mt-2 pt-1 mb-2">
//               Don't have an account?
//               <span className="link-danger" onClick={HandleRegister}>
//                 Register
//               </span>
//             </p>
//           </div>
//         </MDBCol>
//       </MDBRow>
//     </MDBContainer>
//   );
// }

// export default Login;
import React, { Fragment } from "react";
import {
  MDBContainer,
  MDBCol,
  MDBRow,
  MDBBtn,
  MDBIcon,
  MDBInput,
  MDBCheckbox,
  MDBCardHeader,
} from "mdb-react-ui-kit";
import { useState, useContext } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Alert from "react-bootstrap/Alert";
import { UserContext } from "../../contexts/user.context";
import pic from "../Login/6343825.jpg";

function Login() {
  //const token = localStorage.getItem("accessToken");
  const [loginDetials, setLoginDetails] = useState({
    email: "",
    password: "",
    rememberMe: true,
  });

  const [EmailError, setEmailError] = useState(false);
  const [PasswordError, setPasswordError] = useState(false);
  const { currentUser, setCurrentUser } = useContext(UserContext);
  const [formErrors, setFormErrors] = useState({});
  const [status, setStatus] = useState(undefined);
  const navigate = useNavigate();
  const validate = (values) => {
    const errors = {};
    if (!values.email) errors.name = "Email is required";
    if (!values.password) errors.password = "Password is required";

    setFormErrors(errors);
  };
  const handleEmail = (e) => {
    const copiedDetails = { ...loginDetials };
    copiedDetails.email = e.target.value;
    // setLoginDetails((details) => ({
    //   ...copiedDetails,
    // }));

    if (
      validEmail.test(copiedDetails.email) === false &&
      copiedDetails.email.length !== 0
    )
      setEmailError(true);
    else {
      setEmailError(false);
      setLoginDetails((details) => ({
        ...copiedDetails,
      }));
    }
  };
  const handlePassword = (e) => {
    const copiedDetails = { ...loginDetials };
    copiedDetails.password = e.target.value;
    setLoginDetails((details) => ({
      ...copiedDetails,
    }));
  };

  const handleLogin = async (e) => {
    validate(loginDetials);
    e.preventDefault();
    await axios({
      method: "post",
      url: "https://localhost:7259/api/Authentication/Login",
      data: loginDetials,
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (res.data.isSuccess) {
          setStatus({ type: "success" });

          localStorage.setItem(
            "accessToken",
            res.data.result.value.accessToken
          );
          setCurrentUser(res.data.result.value.accessToken);
          console.log("Current User start");
          console.log(currentUser);
          console.log("Current User end");
          console.log(localStorage.getItem("accessToken"));

          navigate("/Dashboard");
        } else {
          setStatus({ type: "error" });
        }
      })
      .catch((error) => {
        setStatus({ type: "error", error });
      });
  };

  const HandleRegister = () => {
    navigate("/");
  };

  return (
    <MDBContainer fluid className="p-3 my-5 h-custom">
      {status?.type === "success" && (
        <Alert key={status} variant={"success"}>
          User Successfully Registered
        </Alert>
      )}
      {status?.type === "error" && (
        <Alert key={status} variant={"danger"}>
          User Login failed.
        </Alert>
      )}
      <MDBRow>
        <MDBCol col="6" md="5">
          <img src={pic} class="img-fluid" alt="Sample" />
        </MDBCol>
        <MDBCol col="5" md="5">
          <p
            className="text h1 fw-bold"
            style={{
              marginTop: "50px",
              fontSize: "30px",
              fontFamily: "sans-serif",
            }}
          >
            Login
          </p>
          {/* <Fragment>
            <MDBInput
              wrapperClass="mb-4"
              label="Email address"
              id="email"
              type="email"
              size="lg"
              onChange={handleEmail}
            />
            <span className="text-danger" style={{ marginRight: "50px" }}>
              {formErrors.email}
            </span>
            {EmailError ? (
              <span className="text-danger" style={{ marginRight: "80px" }}>
                Invalid Email
              </span>
            ) : (
              ""
            )}

            <span className="text-danger">{formErrors.email}</span>
          </Fragment> */}

          <Fragment
            className="d-flex flex-col align-items-center mb-3"
            style={{ display: "flex", flexDirection: "column" }}
          >
            <div>
              <MDBInput
                label="Your Email"
                id="email"
                type="email"
                autoComplete="false"
                onChange={handleEmail}
              />
            </div>
            <span className="text-danger" style={{ marginRight: "50px" }}>
              {formErrors.email}
            </span>
            {EmailError ? (
              <span className="text-danger" style={{ marginRight: "80px" }}>
                Invalid Email
              </span>
            ) : (
              ""
            )}
          </Fragment>

          {/* <div
            className="d-flex flex-col align-items-center mb-3"
            style={{ display: "flex", flexDirection: "column" }}
          >
            <div className="d-flex flex-row align-items-center mb-0">
              <MDBIcon fas icon="envelope me-3" size="lg" />
              <MDBInput
                label="Your Email"
                id="email"
                type="email"
                autoComplete="false"
                onChange={handleEmail}
              />
            </div>
            <span className="text-danger" style={{ marginRight: "50px" }}>
              {formErrors.email}
            </span>
            {EmailError ? (
              <span className="text-danger" style={{ marginRight: "80px" }}>
                Invalid Email
              </span>
            ) : (
              ""
            )}
          </div> */}

          <MDBInput
            wrapperClass="mb-4"
            label="Password"
            id="password"
            type="password"
            size="lg"
            onChange={handlePassword}
          />
          <span className="text-danger">{formErrors.password}</span>
          <div className="d-flex justify-content-between mb-4">
            <MDBCheckbox
              name="flexCheck"
              value=""
              id="flexCheckDefault"
              label="Remember me"
            />
            <a href="!#">Forgot password?</a>
          </div>

          <div className="text-center text-md-start mt-4 pt-2">
            <MDBBtn
              className="mb-0 px-5"
              size="lg"
              onClick={handleLogin}
              style={{ bgColor: "black" }}
            >
              Login
            </MDBBtn>
            <p className="small fw-bold mt-2 pt-1 mb-2">
              Don't have an account?
              <span className="link-danger" onClick={HandleRegister}>
                Register
              </span>
            </p>
          </div>
        </MDBCol>
      </MDBRow>
    </MDBContainer>
  );
}
export const validEmail = new RegExp(
  "^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$"
);
export default Login;
