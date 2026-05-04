import axios from 'axios';
import toast from 'react-hot-toast';

const api = axios.create({
  baseURL: '/api' // Uses proxy in vite config
});

const authApi = axios.create({
  baseURL: '/auth' // For signup/login
});

// Request interceptor to attach token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');

    console.log("🔐 TOKEN SENT:", token); // debug

    if (token) {
      config.headers = {
        ...config.headers,
        Authorization: `Bearer ${token}`,
      };
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor to handle 401
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // DO NOT redirect here to prevent race conditions during OAuth load
    return Promise.reject(error);
  }
);

export const authService = {
  login: (data) => authApi.post('/login', data),
  signup: (data) => authApi.post('/signup', data),
};

export const userService = {
  getProfile: () => api.get('/users/profile'),
  updateProfile: (data) => api.put('/users/profile', data),
  requestEmailChange: (newEmail) => api.put(`/users/request-email-change?newEmail=${newEmail}`),
  changePassword: (data) => api.put('/users/change-password', data),
  forgotPassword: (email) => api.post(`/users/forgot-password?email=${email}`),
  resetPassword: (token, newPassword) => api.post(`/users/reset-password?token=${token}&newPassword=${newPassword}`),
  deleteAccount: (password) => api.delete(password ? `/users/delete?password=${password}` : '/users/delete'),
};

export default api;
