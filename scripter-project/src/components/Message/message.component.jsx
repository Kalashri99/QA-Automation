import React from "react";
import { MDBJumbotron, MDBContainer } from "mdbreact";
import { useLocation } from "react-router-dom";

const Message = () => {
  const location = useLocation();
  const hdg = location.state.heading;
  const msg = location.state.message;
  return (
    <MDBJumbotron fluid>
      <MDBContainer>
        <h2 className="display-4">{hdg}</h2>
        <p className="lead">{msg}</p>
        <p className="lead">Thank You.</p>
      </MDBContainer>
    </MDBJumbotron>
  );
};

export default Message;

// import React, { useState } from "react";
// import Button from "react-bootstrap/Button";
// import Modal from "react-bootstrap/Modal";
// function Example() {
//   const [show, setShow] = useState(false);

//   const handleClose = () => setShow(false);
//   const handleShow = () => setShow(true);

//   return (
//     <>
//       <Button variant="primary" onClick={handleShow}>
//         Launch demo modal
//       </Button>

//       <Modal show={show} onHide={handleClose}>
//         <Modal.Header closeButton>
//           <Modal.Title>Processing...</Modal.Title>
//         </Modal.Header>
//         <Modal.Body>
//           Your File is Sucessfully Dowloaded at ./c/home/dowload
//         </Modal.Body>
//         <Modal.Footer>
//           <Button variant="secondary" onClick={handleClose}>
//             Close
//           </Button>
//         </Modal.Footer>
//       </Modal>
//     </>
//   );
// }
// render(<Example />);
