// import React from "react";
// import {
//   MDBBtn,
//   MDBContainer,
//   MDBRow,
//   MDBCol,
//   MDBCard,
//   MDBCardBody,
//   MDBCardImage,
//   MDBInput,
//   MDBIcon,
//   MDBCheckbox,
// } from "mdb-react-ui-kit";
// import { useState } from "react";
// import axios from "axios";
// import Alert from "react-bootstrap/Alert";
// import { useNavigate } from "react-router-dom";

// function Register() {
//   const [userDetails, setUserDetails] = useState({
//     name: "",
//     userName: "",
//     phoneNumber: "",
//     email: "",
//     password: "",
//     confirmPassword: "",
//   });

//   const [status, setStatus] = useState(undefined);
//   const [isValidEmail, setIsValidEmail] = useState(true);

//   const navigate = useNavigate();

//   const addName = (e) => {
//     let copiedDetails = { ...userDetails };
//     copiedDetails.name = e.target.value;
//     setUserDetails((details) => ({
//       ...copiedDetails,
//     }));
//     console.log(userDetails);
//   };

//   const addUsername = (e) => {
//     let copiedDetails = { ...userDetails };
//     copiedDetails.userName = e.target.value;
//     setUserDetails((details) => ({
//       ...copiedDetails,
//     }));
//     console.log(userDetails);
//   };

//   const addPhone = (e) => {
//     let copiedDetails = { ...userDetails };
//     copiedDetails.phoneNumber = e.target.value;
//     setUserDetails((details) => ({
//       ...copiedDetails,
//     }));
//     console.log(userDetails);
//   };
//   const addEmail = (e) => {
//     let copiedDetails = { ...userDetails };
//     copiedDetails.email = e.target.value;
//     setUserDetails((details) => ({
//       ...copiedDetails,
//     }));
//     console.log(userDetails);
//     const emailRegex = /\S+@\S+\.\S+/;
//     setIsValidEmail(emailRegex.test(copiedDetails.email));
//   };
//   const addPassword = (e) => {
//     let copiedDetails = { ...userDetails };
//     copiedDetails.password = e.target.value;
//     setUserDetails((details) => ({
//       ...copiedDetails,
//     }));
//     console.log(userDetails);
//   };
//   const addConfirmPassword = (e) => {
//     let copiedDetails = { ...userDetails };
//     copiedDetails.confirmPassword = e.target.value;
//     setUserDetails((details) => ({
//       ...copiedDetails,
//     }));
//     console.log(userDetails);
//   };

//   const handleAddUser = async (e) => {
//     // e.preventDefault();
//     console.log(userDetails);
//     await axios({
//       method: "post",
//       url: "https://localhost:7259/api/Authentication/Register",
//       data: userDetails,
//       headers: {
//         "Content-Type": "application/json",
//       },
//     })
//       .then((res) => {
//         if (res.data.isSuccess) {
//           setStatus({ type: "success" });
//           navigate("login");
//         } else {
//           setStatus({ type: "error" });
//         }
//       })
//       .catch((error) => {
//         setStatus({ type: "error", error });
//       });
//   };
//   const HandleLogin = () => {
//     navigate("/Login");
//   };

//   return (
//     <MDBContainer fluid>
//       {status?.type === "success" && (
//         <Alert key={status} variant={"success"}>
//           User Successfully Registered
//         </Alert>
//       )}
//       {status?.type === "error" && (
//         <Alert key={status} variant={"danger"}>
//           User Registration failed.
//         </Alert>
//       )}

//       <MDBCard className="text-black m-5" style={{ borderRadius: "25px" }}>
//         <MDBCardBody>
//           <MDBRow>
//             <MDBCol
//               md="10"
//               lg="6"
//               className="order-2 order-lg-1 d-flex flex-column align-items-center"
//             >
//               <p classNAme="text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4">
//                 Sign Up
//               </p>

//               <div className="d-flex flex-row align-items-center mb-4 ">
//                 <MDBIcon fas icon="user me-3" size="lg" />
//                 <MDBInput
//                   label="Your Name"
//                   id="name"
//                   type="text"
//                   className="w-100"
//                   onChange={addName}
//                 />
//               </div>
//               <div className="d-flex flex-row align-items-center mb-4 ">
//                 <MDBIcon fas icon="user me-3" size="lg" />
//                 <MDBInput
//                   label="Your username"
//                   id="username"
//                   type="text"
//                   className="w-100"
//                   onChange={addUsername}
//                 />
//               </div>
//               <div className="d-flex flex-row align-items-center mb-4 ">
//                 <MDBIcon fas icon="phone " size="lg" />
//                 <MDBInput
//                   label="Your phone number"
//                   id="phone"
//                   type="text"
//                   className="w-100"
//                   onChange={addPhone}
//                 />
//               </div>

//               <div className="d-flex flex-row align-items-center mb-4">
//                 <MDBIcon fas icon="envelope me-3" size="lg" />
//                 <MDBInput
//                   label="Your Email"
//                   id="email"
//                   type="email"
//                   autoComplete="false"
//                   onChange={addEmail}
//                 />
//                 {!isValidEmail && <p>Please enter a valid email address.</p>}
//               </div>

//               <div className="d-flex flex-row align-items-center mb-4">
//                 <MDBIcon fas icon="lock me-3" size="lg" />
//                 <MDBInput
//                   label="Password"
//                   id="password"
//                   type="password"
//                   onChange={addPassword}
//                 />
//               </div>

//               <div className="d-flex flex-row align-items-center mb-4">
//                 <MDBIcon fas icon="key me-3" size="lg" />
//                 <MDBInput
//                   label="Repeat your password"
//                   id="confirmPassword"
//                   type="password"
//                   onChange={addConfirmPassword}
//                 />
//               </div>

//               <MDBBtn className="mb-4" size="lg" onClick={handleAddUser}>
//                 Register
//               </MDBBtn>
//               <p className="mb-5 pb-lg-2" style={{ color: "#393f81" }}>
//                 Already have an account?
//                 <span style={{ color: "#393f81" }} onClick={HandleLogin}>
//                   Login Here
//                 </span>
//               </p>
//             </MDBCol>

//             <MDBCol
//               md="10"
//               lg="6"
//               className="order-1 order-lg-2 d-flex align-items-center"
//             >
//               <MDBCardImage
//                 src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-registration/draw1.webp"
//                 fluid
//               />
//             </MDBCol>
//           </MDBRow>
//         </MDBCardBody>
//       </MDBCard>
//     </MDBContainer>
//   );
// }

// export default Register;
import React from "react";
import {
  MDBBtn,
  MDBContainer,
  MDBRow,
  MDBCol,
  MDBCard,
  MDBCardBody,
  MDBCardImage,
  MDBInput,
  MDBIcon,
  MDBCheckbox,
} from "mdb-react-ui-kit";
import { useState } from "react";
import axios from "axios";
import Alert from "react-bootstrap/Alert";
import { useNavigate, useRouteError } from "react-router-dom";
import pic from "../Register/6343845.jpg";

function Register() {
  const [userDetails, setUserDetails] = useState({
    name: "",
    userName: "",
    phoneNumber: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const [status, setStatus] = useState(undefined);
  const [Usernameerror, setUserError] = useState(false);
  const [NameError, setNameError] = useState(false);
  const [PasswordError, setPasswordError] = useState(false);
  const [PhoneNoError, setPhonenoError] = useState(false);
  const [EmailError, setEmailError] = useState(false);
  const [confirmedpasswordError, setConfirmedPassword] = useState(false);
  const navigate = useNavigate();

  const [formErrors, setFormErrors] = useState({});

  const validate = (values) => {
    const errors = {};
    if (!values.name) errors.name = "name is required";
    if (!values.userName) errors.userName = "username is required";

    if (!values.phoneNumber) errors.phoneNumber = "phone number is required";

    if (!values.email) errors.email = "email is required";

    if (!values.password) errors.password = "password is required";

    if (!values.confirmPassword)
      errors.confirmPassword = "confirmPassword is required";

    if (values.password !== values.confirmPassword && values.confirmPassword)
      errors.passMatch = "Password and confirm password should be same";

    setFormErrors(errors);
  };
  const addName = (e) => {
    let copiedDetails = { ...userDetails };
    copiedDetails.name = e.target.value;
    if (copiedDetails.name.length > 10) setNameError(true);
    else {
      setNameError(false);
      setUserDetails((details) => ({
        ...copiedDetails,
      }));
      console.log(userDetails);
    }
  };
  const addUsername = (e) => {
    let copiedDetails = { ...userDetails };
    copiedDetails.userName = e.target.value;
    if (copiedDetails.userName.length === 0) setUserError(true);
    else {
      setUserError(false);
      setUserDetails((details) => ({
        ...copiedDetails,
      }));
      console.log(userDetails);
    }
  };

  const addPhone = (e) => {
    const reg = /^\d[0-9]{9}$/;
    let copiedDetails = { ...userDetails };
    copiedDetails.phoneNumber = e.target.value;
    if (
      reg.test(copiedDetails.phoneNumber) === false &&
      copiedDetails.phoneNumber.length !== 0
    )
      setPhonenoError(true);
    else {
      setPhonenoError(false);
      setUserDetails((details) => ({
        ...copiedDetails,
      }));
      console.log(userDetails);
    }
  };
  const addEmail = (e) => {
    let copiedDetails = { ...userDetails };
    copiedDetails.email = e.target.value;
    if (
      validEmail.test(copiedDetails.email) === false &&
      copiedDetails.email.length !== 0
    )
      setEmailError(true);
    else {
      setEmailError(false);
      setUserDetails((details) => ({
        ...copiedDetails,
      }));
      console.log(userDetails);
    }
  };
  const addPassword = (e) => {
    let copiedDetails = { ...userDetails };
    copiedDetails.password = e.target.value;
    setUserDetails((details) => ({
      ...copiedDetails,
    }));
    console.log(userDetails);
  };
  const addConfirmPassword = (e) => {
    let copiedDetails = { ...userDetails };
    copiedDetails.confirmPassword = e.target.value;

    setUserDetails((details) => ({
      ...copiedDetails,
    }));
    console.log(userDetails);
  };
  const handleAddUser = async (e) => {
    //e.preventDefault();
    validate(userDetails);
    // if (validEmail()) {
    console.log(userDetails);
    await axios({
      method: "post",
      url: "https://localhost:7259/api/Authentication/Register",
      data: userDetails,
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (res.data.isSuccess) {
          setStatus({ type: "success" });
          navigate("login");
        } else {
          setStatus({ type: "error" });
        }
      })
      .catch((error) => {
        setStatus({ type: "error", error });
      });
    //}
  };
  const HandleLogin = () => {
    navigate("/Login");
  };

  return (
    <MDBContainer fluid>
      {status?.type === "success" && (
        <Alert key={status} variant={"success"}>
          User Successfully Registered
        </Alert>
      )}
      {status?.type === "error" && (
        <Alert key={status} variant={"danger"}>
          User Registration failed.
        </Alert>
      )}

      <MDBCard className="text-black m-4" style={{ borderRadius: "25px" }}>
        <MDBCardBody>
          <MDBRow>
            <MDBCol
              md="10"
              lg="6"
              className="order-2 order-lg-1 d-flex flex-column align-items-center"
            >
              <p
                classNAme="text-center h1 fw-bold"
                style={{ fontSize: "30px", fontFamily: "sans-serif" }}
              >
                Sign Up
              </p>
              <div
                className="d-flex flex-col align-items-center mb-3"
                style={{ display: "flex", flexDirection: "column" }}
              >
                <div className="d-flex flex-row align-items-center mb-0">
                  <MDBIcon fas icon="user me-3" size="lg" />
                  <MDBInput
                    required
                    label="Your Name"
                    id="name"
                    type="text"
                    className="w-100"
                    onChange={addName}
                  />
                </div>
                <span className="text-danger" style={{ marginRight: "50px" }}>
                  {formErrors.name}
                </span>
              </div>
              <div
                className="d-flex flex-col align-items-center mb-3"
                style={{ display: "flex", flexDirection: "column" }}
              >
                <div className="d-flex flex-row align-items-center mb-0">
                  <MDBIcon fas icon="user me-3" size="lg" />
                  <MDBInput
                    label="Your username"
                    id="username"
                    type="text"
                    className="w-100"
                    onChange={addUsername}
                  />
                </div>
                <span className="text-danger" style={{ marginRight: "30px" }}>
                  {formErrors.userName}
                </span>
              </div>
              <div
                className="d-flex flex-col align-items-center mb-3 "
                style={{ display: "flex", flexDirection: "column" }}
              >
                <div className="d-flex flex-row align-items-center mb-0">
                  <MDBIcon
                    fas
                    icon="phone "
                    size="lg"
                    style={{ marginRight: "15px" }}
                  />
                  <MDBInput
                    label="Your phone number"
                    id="phone"
                    type="text"
                    className="w-100"
                    onChange={addPhone}
                  />
                </div>
                <span className="text-danger" style={{ marginLeft: "10px" }}>
                  {formErrors.phoneNumber}
                </span>
                {PhoneNoError ? (
                  <p className="text-danger" style={{ marginRight: "20px" }}>
                    Invalid Phone Number
                  </p>
                ) : (
                  ""
                )}
              </div>

              <div
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
                    onChange={addEmail}
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
              </div>

              <div
                className="d-flex flex-col align-items-center mb-3"
                style={{ display: "flex", flexDirection: "column" }}
              >
                <div className="d-flex flex-row align-items-center mb-0">
                  <MDBIcon fas icon="lock me-3" size="lg" />
                  <MDBInput
                    label="Password"
                    id="password"
                    type="password"
                    onChange={addPassword}
                  />
                </div>
                <span className="text-danger" style={{ marginRight: "22px" }}>
                  {formErrors.password}
                </span>
              </div>

              <div
                className="d-flex flex-col align-items-center mb-3"
                style={{ display: "flex", flexDirection: "column" }}
              >
                <div className="d-flex flex-row align-items-center mb-0">
                  <MDBIcon fas icon="key me-3" size="lg" />
                  <MDBInput
                    label="Repeat your password"
                    id="confirmPassword"
                    type="password"
                    onChange={addConfirmPassword}
                  />
                </div>
                <span className="text-danger" style={{ marginLeft: "31px" }}>
                  {formErrors.confirmPassword}
                </span>
                <span className="text-danger">{formErrors.passMatch}</span>
              </div>
              <MDBBtn className="mb-4" size="lg" onClick={handleAddUser}>
                Register
              </MDBBtn>
              <p className="mb-5 pb-lg-2" style={{ color: "#393f81" }}>
                Already have an account?
                <span style={{ color: "#393f81" }} onClick={HandleLogin}>
                  <span className="link-danger">Login Here</span>
                </span>
              </p>
            </MDBCol>

            <MDBCol
              md="10"
              lg="6"
              className="order-1 order-lg-2 d-flex align-items-center"
            >
              <MDBCardImage class="img-fluid" src={pic} />
            </MDBCol>
          </MDBRow>
        </MDBCardBody>
      </MDBCard>
    </MDBContainer>
  );
}
export const validEmail = new RegExp(
  "^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$"
);
export const validPassword = new RegExp("^(?=.*?[A-Za-z])(?=.*?[0-9]).{6,}$");
export default Register;
