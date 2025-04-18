import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/common/home/Home';
import Login from './pages/common/signin-form/Login';
import Register from './pages/common/signup-form/Register';
import ResetPassword from './pages/common/reset-password/ResetPassword';
import ChangePassword from './pages/common/confirm-password/ChangePassword';
import './App.css';

function App() {
  return (
    <BrowserRouter>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/signin" element={<Login/ >} />
      <Route path="/signup" element={<Register/>} />
      <Route path="/reset-password" element={<ResetPassword/>} />
      <Route path="/confirm-password" element={<ChangePassword/>} />

    </Routes>
  </BrowserRouter>
  );
}

export default App;
