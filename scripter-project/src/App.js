import "./App.css";
import Login from "./components/Login/login.component";
import Navbar from "./components/Navbar/navbar.component";
import Register from "./components/Register/register.component";
import Footer from "./components/Footer/footer.component";
import Dashboard from "./components/Dashboard/dashboard.component";
import { Route, Routes } from "react-router-dom";
import Loading from "./components/Loading/loading.component";
import Message from "./components/Message/message.component";
import ExcelDashboard from "./components/Dashboard/excelDashboard.component";
import ReportDashboard from "./components/Dashboard/reportDashboard.component";
import Executing from "./components/Loading/executing.component";

function App() {
  return (
    <Routes>
      <Route className="head" path="/" element={<Navbar />}>
        <Route index element={<Register />} />
        <Route path="Login" element={<Login />} />
        <Route path="Dashboard" element={<Dashboard />} />
        <Route path="ExcelDashboard" element={<ExcelDashboard />} />
        <Route path="ReportDashboard" element={<ReportDashboard />} />
        <Route path="Loading" element={<Loading />} />
        <Route path="Message" element={<Message />} />
        <Route path="Executing" element={<Executing />} />
      </Route>
    </Routes>
  );
}

export default App;
