import axios from 'axios';

const API_BASE_URL = process.env.NODE_ENV === 'production' 
  ? 'http://localhost:4004' 
  : 'http://localhost:4004';

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Auth Service APIs
export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  validate: (token) => api.get('/auth/validate', {
    headers: { Authorization: `Bearer ${token}` }
  }),
};

// Patient Service APIs  
export const patientAPI = {
  getAll: () => api.get('/api/patients'),
  getById: (id) => api.get(`/api/patients/${id}`),
  create: (patient) => api.post('/api/patients', patient),
  update: (id, patient) => api.put(`/api/patients/${id}`, patient),
  delete: (id) => api.delete(`/api/patients/${id}`),
};

// Doctor Service APIs
export const doctorAPI = {
  getAll: () => api.get('/api/doctors'),
  getById: (id) => api.get(`/api/doctors/${id}`),
  create: (doctor) => api.post('/api/doctors', doctor),
  update: (id, doctor) => api.put(`/api/doctors/${id}`, doctor),
  delete: (id) => api.delete(`/api/doctors/${id}`),
  getBySpecialization: (spec) => api.get(`/api/doctors/specialization/${spec}`),
};

// Appointment APIs
export const appointmentAPI = {
  book: (appointment) => api.post('/api/appointments', appointment),
  getByDoctor: (doctorId) => api.get(`/api/appointments/doctor/${doctorId}`),
  getByPatient: (patientId) => api.get(`/api/appointments/patient/${patientId}`),
  getPatientDetails: (patientId) => api.get(`/api/appointments/patient-details/${patientId}`),
};

// Direct service calls (fallback)
export const directAPI = {
  // Direct calls to services (bypass API Gateway)
  patients: {
    getAll: () => axios.get('/patients'),
    create: (patient) => axios.post('/patients', patient),
  },
  doctors: {
    getAll: () => axios.get('/doctors'),
    create: (doctor) => axios.post('/doctors', doctor),
  },
  appointments: {
    book: (appointment) => axios.post('/appointments', appointment),
    getPatientDetails: (patientId) => axios.get(`/appointments/patient-details/${patientId}`),
  },
  auth: {
    login: (credentials) => axios.post('http://localhost:4005/test/login', {}),
  },
};

export default api;