import Header from "./components/Header";
import Home from "./pages/Home";
import SignUp from "./pages/SignUp";
import Login from "./pages/Login";
import Explore from "./pages/Explore";
import Create from "./pages/Create";
import Topic from "./pages/Topic";
import { Route, Routes } from "react-router-dom";

export default function App() {
  return (
    <>
      <Header />
      <main className="content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/login" element={<Login />} />
          <Route path="/explore" element={<Explore />} />
          <Route path="/create" element={<Create />} />
          <Route path="/topic" element={<Topic />} />
        </Routes>
      </main>
    </>
  );
}
