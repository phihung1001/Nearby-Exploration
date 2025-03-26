import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/common/home/Home';
import Login from './pages/common/signin-form/Login';
import Register from './pages/common/signup-form/Register';
import ResetPassword from './pages/common/reset-password/ResetPassword';
import ChangePassword from './pages/common/confirm-password/ChangePassword';
import RestaurantDetail from './pages/common/restaurant-detail/RestaurantDetail';
import './App.css';
import FoodDetail from './pages/common/food-detail/FoodDetail';
import RestaurantListPage from "./pages/common/RestaurantListPage";
import Profile from './pages/user/profile/Profile';

function App() {
  return (
    <BrowserRouter>
    <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/auth/signin" element={<Login/ >} />
        <Route path="/auth/signup" element={<Register/>} />
        <Route path="/auth/reset-password" element={<ResetPassword/>} />
        <Route path="/auth/confirm-password" element={<ChangePassword/>} />
        <Route path="/public/food-list" element={<Login />} />
        <Route path="/public/restaurant-detail/:id" element={<RestaurantDetail />} />  
        <Route path="/public/restaurant/:restaurantId/food-detail/:foodId" element={<FoodDetail />} />
        <Route path="/public/restaurant-list" element={<RestaurantListPage />} />
        <Route path="/profile/:id" element={<Profile />} />

    </Routes>
  </BrowserRouter>
  );
}

export default App;
