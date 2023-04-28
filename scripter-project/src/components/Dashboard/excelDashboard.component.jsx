import React, { Fragment, useState } from "react";
import { MDBContainer } from "mdb-react-ui-kit";
import FileUpload from "../FileUpload/fileUpload.component";
import ExcelFileView from "../FileView/excelView.component";
export default function ExcelDashboard() {
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
        <h1>Welcome to the excel Dashboard</h1>
        <ExcelFileView handleISConvert={handleISConvert} />
      </Fragment>
    </MDBContainer>
  );
}
