import React, { Fragment, useState } from "react";
import { MDBContainer } from "mdb-react-ui-kit";
import FileUpload from "../FileUpload/fileUpload.component";
import FileView from "../FileView/fileView.component";
import Loading from "../Loading/loading.component";
export default function Dashboard() {
  const [isConvert, setconvert] = useState(false);

  const handleISConvert = (input) => {
    //e.preventDefault();
    setconvert(input);
    const printConvert = () => {
      console.log(isConvert);
    };
    printConvert();
  };

  return (
    <MDBContainer>
      <Fragment>
        <h1>Welcome to the Dashboard</h1>
        <FileUpload />
        <FileView handleISConvert={handleISConvert} />
      </Fragment>
    </MDBContainer>
  );
}
