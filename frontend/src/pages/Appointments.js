import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Form, Button, Alert, Table } from 'react-bootstrap';
import { directAPI } from '../services/api';

function Appointments() {
  const [patients, setPatients] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [patientDetails, setPatientDetails] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [appointment, setAppointment] = useState({
    patientId: '',
    doctorId: '',
    appointmentDate: '',
    appointmentTime: '',
    reason: '',
    notes: ''
  });

  useEffect(() => {
    loadPatientsAndDoctors();
  }, []);

  const loadPatientsAndDoctors = async () => {
    try {
      const [patientsRes, doctorsRes] = await Promise.all([
        directAPI.patients.getAll(),
        directAPI.doctors.getAll()
      ]);
      setPatients(patientsRes.data);
      setDoctors(doctorsRes.data);
    } catch (error) {
      setError('Failed to load patients and doctors');
      console.error('Error loading data:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      const response = await directAPI.appointments.book(appointment);
      setSuccess('Appointment booked successfully!');
      setAppointment({
        patientId: '',
        doctorId: '',
        appointmentDate: '',
        appointmentTime: '',
        reason: '',
        notes: ''
      });
      console.log('Appointment created:', response.data);
    } catch (error) {
      setError('Failed to book appointment. Please check if patient and doctor exist.');
      console.error('Error booking appointment:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setAppointment({
      ...appointment,
      [e.target.name]: e.target.value
    });
  };

  const loadPatientDetails = async (patientId) => {
    if (!patientId) {
      setPatientDetails(null);
      return;
    }

    try {
      const response = await directAPI.appointments.getPatientDetails(patientId);
      setPatientDetails(response.data);
    } catch (error) {
      console.error('Error loading patient details:', error);
      setPatientDetails(null);
    }
  };

  const handlePatientChange = (e) => {
    const patientId = e.target.value;
    setAppointment({
      ...appointment,
      patientId: patientId
    });
    loadPatientDetails(patientId);
  };

  return (
    <div>
      <h1 className="mb-4">📅 Appointments Management</h1>

      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}

      <Row>
        <Col md={8}>
          <Card>
            <Card.Header>
              <h5>Book New Appointment</h5>
            </Card.Header>
            <Card.Body>
              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Select Patient</Form.Label>
                      <Form.Select
                        name="patientId"
                        value={appointment.patientId}
                        onChange={handlePatientChange}
                        required
                      >
                        <option value="">Choose a patient...</option>
                        {patients.map((patient) => (
                          <option key={patient.id} value={patient.id}>
                            {patient.name} - {patient.email}
                          </option>
                        ))}
                      </Form.Select>
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Select Doctor</Form.Label>
                      <Form.Select
                        name="doctorId"
                        value={appointment.doctorId}
                        onChange={handleChange}
                        required
                      >
                        <option value="">Choose a doctor...</option>
                        {doctors.map((doctor) => (
                          <option key={doctor.id} value={doctor.id}>
                            {doctor.name} - {doctor.specialization}
                          </option>
                        ))}
                      </Form.Select>
                    </Form.Group>
                  </Col>
                </Row>

                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Appointment Date</Form.Label>
                      <Form.Control
                        type="date"
                        name="appointmentDate"
                        value={appointment.appointmentDate}
                        onChange={handleChange}
                        min={new Date().toISOString().split('T')[0]}
                        required
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Appointment Time</Form.Label>
                      <Form.Control
                        type="time"
                        name="appointmentTime"
                        value={appointment.appointmentTime}
                        onChange={handleChange}
                        required
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label>Reason for Visit</Form.Label>
                  <Form.Control
                    type="text"
                    name="reason"
                    value={appointment.reason}
                    onChange={handleChange}
                    placeholder="e.g., Regular checkup, Follow-up, Emergency"
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Notes (Optional)</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    name="notes"
                    value={appointment.notes}
                    onChange={handleChange}
                    placeholder="Additional notes or special instructions"
                  />
                </Form.Group>

                <Button
                  variant="primary"
                  type="submit"
                  disabled={loading}
                  className="w-100"
                >
                  {loading ? 'Booking Appointment...' : '📅 Book Appointment'}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          {patientDetails && (
            <Card>
              <Card.Header>
                <h6>Patient Details & History</h6>
              </Card.Header>
              <Card.Body>
                <div className="mb-3">
                  <strong>Patient Info:</strong>
                  <div>Name: {patientDetails.patient.name}</div>
                  <div>Email: {patientDetails.patient.email}</div>
                  <div>DOB: {patientDetails.patient.dateOfBirth}</div>
                </div>

                <div className="mb-3">
                  <strong>Appointment History:</strong>
                  <div className="text-muted">
                    Total Appointments: {patientDetails.totalAppointments}
                  </div>
                </div>

                {patientDetails.appointments && patientDetails.appointments.length > 0 && (
                  <div>
                    <strong>Recent Appointments:</strong>
                    <div style={{ maxHeight: '200px', overflowY: 'auto' }}>
                      {patientDetails.appointments.slice(0, 3).map((apt, index) => (
                        <div key={index} className="border-bottom py-2">
                          <small>
                            <div>{apt.doctorName}</div>
                            <div>{apt.appointmentDate} at {apt.appointmentTime}</div>
                            <div className="text-muted">{apt.reason}</div>
                          </small>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </Card.Body>
            </Card>
          )}

          <Card className="mt-3">
            <Card.Header>
              <h6>Quick Stats</h6>
            </Card.Header>
            <Card.Body>
              <div>👥 Total Patients: {patients.length}</div>
              <div>👨⚕️ Total Doctors: {doctors.length}</div>
              <div>🏥 Services Available: 6</div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
}

export default Appointments;