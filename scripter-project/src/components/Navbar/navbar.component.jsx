import React, { useState, useContext } from "react";
import {
  MDBNavbar,
  MDBContainer,
  MDBIcon,
  MDBNavbarNav,
  MDBNavbarItem,
  MDBNavbarLink,
  MDBNavbarToggler,
  MDBNavbarBrand,
  MDBCollapse,
} from "mdb-react-ui-kit";
import { useNavigate, Outlet } from "react-router-dom";
import { UserContext } from "../../contexts/user.context";
import axios from "axios";
import pic from "../Navbar/icon.png";
export default function Navbar(props) {
  const navigate = useNavigate();
  const { currentUser, setCurrentUser } = useContext(UserContext);
  console.log("Current User");
  console.log(currentUser);
  //console.log(props);
  const [showNavColor, setShowNavColor] = useState(false);
  //localStorage.setItem("isLoggedIn", false);
  const isLoggedIn = localStorage.getItem("isLoggedIn");
  //console.log(isLoggedIn);

  const handleLogout = async (e) => {
    e.preventDefault();
    await axios({
      method: "delete",
      url: "https://localhost:7259/api/Authentication/Logout",
      headers: {
        Authorization: `bearer ${localStorage.getItem("accessToken")}`,
        "Content-Type": "application/json",
      },
    }).then(() => {
      localStorage.removeItem("accessToken");
      setCurrentUser(null);
    });
    navigate("/Login");
  };

  // const handleLogin = () => {
  //   navigate("/Login");
  // };

  const handleHome = () => {
    if (localStorage.getItem("accessToken")) {
      navigate("/Dashboard");
    } else {
      navigate("/Login");
    }
  };

  const handleExcelDashboard = () => {
    if (localStorage.getItem("accessToken")) {
      navigate("/ExcelDashboard");
    } else {
      navigate("/Login");
    }
  };
  const handleReportDashboard = () => {
    if (localStorage.getItem("accessToken")) {
      navigate("/ReportDashboard");
    } else {
      navigate("/Login");
    }
  };

  return (
    <>
      <MDBNavbar sticky expand="lg" dark bgColor="primary">
        <MDBContainer fluid>
          <MDBNavbarBrand href="#" onClick={handleHome}>
            AutoTester
            <div>
              <img
                src={pic}
                height="40px"
                style={{ padding: "5px" }}
                alt="Home"
              ></img>
            </div>
          </MDBNavbarBrand>
          <MDBNavbarToggler
            type="button"
            data-target="#navbarColor02"
            aria-controls="navbarColor02"
            aria-expanded="false"
            aria-label="Toggle navigation"
            onClick={() => setShowNavColor(!showNavColor)}
          >
            <MDBIcon icon="bars" fas />
          </MDBNavbarToggler>
          <MDBCollapse show={showNavColor} navbar>
            <MDBNavbarNav className="me-auto mb-2 mb-lg-0">
              {/* <MDBNavbarItem className="active">
                <MDBNavbarLink aria-current="page" onClick={handleHome}>
                  Home
                </MDBNavbarLink>
              </MDBNavbarItem> */}

              {localStorage.getItem("accessToken") ? (
                <MDBNavbarItem className="active">
                  <MDBNavbarLink
                    aria-current="page"
                    onClick={handleExcelDashboard}
                  >
                    Excel Dashboard
                  </MDBNavbarLink>
                </MDBNavbarItem>
              ) : null}

              {localStorage.getItem("accessToken") ? (
                <MDBNavbarItem className="active">
                  <MDBNavbarLink
                    aria-current="page"
                    onClick={handleReportDashboard}
                  >
                    Report Dashboard
                  </MDBNavbarLink>
                </MDBNavbarItem>
              ) : null}

              {localStorage.getItem("accessToken") ? (
                <MDBNavbarItem>
                  <MDBNavbarLink onClick={handleLogout}>Logout</MDBNavbarLink>
                </MDBNavbarItem>
              ) : null}
            </MDBNavbarNav>
          </MDBCollapse>
        </MDBContainer>
      </MDBNavbar>
      <Outlet />
    </>
  );
}
