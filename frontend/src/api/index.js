import axios from 'axios';
import toast from 'react-hot-toast';

export const API_URL = '/api';
export const AUTH_URL = '/auth';

const api = axios.create({
  baseURL: API_URL // Uses proxy in vite config
});

const authApi = axios.create({
  baseURL: AUTH_URL // For signup/login
});

// Request interceptor to attach token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');

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
  changePassword: (data) => api.put('/users/change-password', data),
  forgotPassword: (email) => api.post(`/users/forgot-password?email=${email}`),
  resetPassword: (token, newPassword) => api.post(`/users/reset-password?token=${token}&newPassword=${newPassword}`),
  deleteAccount: (password) => api.delete(password ? `/users/delete?password=${password}` : '/users/delete')
};

export const groupService = {
  createGroup: (groupName) => api.post('/groups', groupName),
  getMyGroup: () => api.get(`/groups/my-group`),
  addUserToGroup: (username) => api.put(`/groups/add-student/${username}`),
  deleteUserFromGroup: (id) => api.delete(`/groups/delete-student/${id}`),
  deleteGroup: () => api.delete(`/groups`)
}

export const subjectService = {
  getSubjects: () => api.get('/training-subjects'),
  getSubject: (id) => api.get(`/training-subjects/${id}`),
  createSubject: (subjectData) => api.post('/training-subjects', subjectData),
  deleteSubject: (id) => api.delete(`/training-subjects/${id}`),
}

export const educationalMaterialService = {
  getMaterials: (id) => api.get(`/educational-materials/subject/${id}`),
  createMaterial: (materialData) => api.post('/educational-materials', materialData),
  updateMaterial: (id, materialData) => api.put(`/educational-materials/${id}`, materialData),
  deleteMaterial: (id) => api.delete(`/educational-materials/${id}`),
}

export const materialCompleteService = {
  getStatus: (materialId) => api.get(`/material-complete/${materialId}`),
  updateStatus: (materialId, status) => api.put(`/material-complete/${materialId}`, { status }),
}

export default api;
