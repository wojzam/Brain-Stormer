import Home from "./pages/Home";
import Explore from "./pages/Explore";
import Create from "./pages/Create";
import Topic from "./pages/Topic";
import { Routes, Route } from "react-router-dom";

export default function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/explore" element={<Explore />} />
        <Route path="/create" element={<Create />} />
        <Route path="/topic" element={<Topic />} />
      </Routes>
    </>
  );
}
