import React, { Fragment, useState } from "react";
import { MDBContainer } from "mdb-react-ui-kit";

import ReportView from "../FileView/reportView.component";
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
        <h1>Welcome to the Report Dashboard</h1>
        <ReportView handleISConvert={handleISConvert} />
      </Fragment>
    </MDBContainer>
  );
}
