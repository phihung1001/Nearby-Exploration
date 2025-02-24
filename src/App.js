import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/common/home/Home';
import Login from './pages/common/signin-form/Login';
import Register from './pages/common/signup-form/Register';
import ResetPassword from './pages/common/reset-password/ResetPassword';
import ChangePassword from './pages/common/confirm-password/ChangePassword';
import RestaurantDetail from './pages/common/restaurant-detail/RestaurantDetail';
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
        <Route path="/food-list" element={<Login />} />
        <Route path="/restaurant-detail/:id" element={<RestaurantDetail />} />  
    </Routes>
  </BrowserRouter>
  );
}

export default App;
