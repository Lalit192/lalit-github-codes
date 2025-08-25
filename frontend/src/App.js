import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Table, Form, Modal, Alert } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

function App() {
  const [patients, setPatients] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [showPatientModal, setShowPatientModal] = useState(false);
  const [showDoctorModal, setShowDoctorModal] = useState(false);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('');

  const [newPatient, setNewPatient] = useState({
    name: '',
    email: '',
    dateOfBirth: '',
    address: '',
    registeredDate: new Date().toISOString().split('T')[0]
  });

  const [newDoctor, setNewDoctor] = useState({
    name: '',
    email: '',
    specialization: 'Cardiology',
    phoneNumber: '',
    licenseNumber: '',
    experienceYears: '',
    joinedDate: new Date().toISOString().split('T')[0],
    department: '',
    address: ''
  });

  useEffect(() => {
    loadPatients();
    loadDoctors();
  }, []);

  const showMessage = (msg, type) => {
    setMessage(msg);
    setMessageType(type);
    setTimeout(() => setMessage(''), 3000);
  };

  const loadPatients = async () => {
    try {
      const response = await axios.get('/api/patients');
      setPatients(response.data);
    } catch (error) {
      showMessage('Failed to load patients', 'danger');
    }
  };

  const loadDoctors = async () => {
    // Doctor service disabled for now
    setDoctors([]);
  };

  const createPatient = async (e) => {
    e.preventDefault();
    try {
      await axios.post('/api/patients', newPatient);
      showMessage('✅ Patient created successfully! Billing account auto-created via gRPC!', 'success');
      setShowPatientModal(false);
      setNewPatient({
        name: '',
        email: '',
        dateOfBirth: '',
        address: '',
        registeredDate: new Date().toISOString().split('T')[0]
      });
      loadPatients();
    } catch (error) {
      showMessage('Failed to create patient', 'danger');
    }
  };

  const createDoctor = async (e) => {
    e.preventDefault();
    showMessage('Doctor service temporarily disabled', 'warning');
  };

  return (
    <Container className="mt-4">
      <Row className="mb-4">
        <Col>
          <h1 className="text-center">🏥 Hospital Management System</h1>
          <p className="text-center text-muted">Patient Service + Doctor Service + Billing Service (gRPC)</p>
        </Col>
      </Row>

      {message && (
        <Alert variant={messageType} className="mb-4">
          {message}
        </Alert>
      )}

      {/* Statistics */}
      <Row className="mb-4">
        <Col md={4}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>👥 Patients</Card.Title>
              <h2 className="text-primary">{patients.length}</h2>
              <Button variant="primary" onClick={() => setShowPatientModal(true)}>
                Add Patient
              </Button>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>👨⚕️ Doctors</Card.Title>
              <h2 className="text-success">{doctors.length}</h2>
              <Button variant="success" onClick={() => setShowDoctorModal(true)}>
                Add Doctor
              </Button>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>💰 Billing</Card.Title>
              <h2 className="text-warning">gRPC</h2>
              <small>Auto-created with patients</small>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Patients Table */}
      <Row className="mb-4">
        <Col>
          <Card>
            <Card.Header>
              <h5>👥 Patients ({patients.length})</h5>
            </Card.Header>
            <Card.Body>
              <Table striped bordered hover responsive>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Date of Birth</th>
                    <th>Address</th>
                    <th>Registered</th>
                  </tr>
                </thead>
                <tbody>
                  {patients.map((patient) => (
                    <tr key={patient.id}>
                      <td>{patient.name}</td>
                      <td>{patient.email}</td>
                      <td>{patient.dateOfBirth}</td>
                      <td>{patient.address}</td>
                      <td>{patient.registeredDate}</td>
                    </tr>
                  ))}
                  {patients.length === 0 && (
                    <tr>
                      <td colSpan="5" className="text-center">No patients found</td>
                    </tr>
                  )}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Doctors Table */}
      <Row>
        <Col>
          <Card>
            <Card.Header>
              <h5>👨⚕️ Doctors ({doctors.length})</h5>
            </Card.Header>
            <Card.Body>
              <Table striped bordered hover responsive>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Specialization</th>
                    <th>Department</th>
                    <th>Experience</th>
                    <th>Phone</th>
                    <th>License</th>
                  </tr>
                </thead>
                <tbody>
                  {doctors.map((doctor) => (
                    <tr key={doctor.id}>
                      <td>{doctor.name}</td>
                      <td>{doctor.specialization}</td>
                      <td>{doctor.department}</td>
                      <td>{doctor.experienceYears} years</td>
                      <td>{doctor.phoneNumber}</td>
                      <td>{doctor.licenseNumber}</td>
                    </tr>
                  ))}
                  {doctors.length === 0 && (
                    <tr>
                      <td colSpan="6" className="text-center">No doctors found</td>
                    </tr>
                  )}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Patient Modal */}
      <Modal show={showPatientModal} onHide={() => setShowPatientModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add New Patient</Modal.Title>
        </Modal.Header>
        <Form onSubmit={createPatient}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Full Name</Form.Label>
              <Form.Control
                type="text"
                value={newPatient.name}
                onChange={(e) => setNewPatient({...newPatient, name: e.target.value})}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                value={newPatient.email}
                onChange={(e) => setNewPatient({...newPatient, email: e.target.value})}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Date of Birth</Form.Label>
              <Form.Control
                type="date"
                value={newPatient.dateOfBirth}
                onChange={(e) => setNewPatient({...newPatient, dateOfBirth: e.target.value})}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Address</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                value={newPatient.address}
                onChange={(e) => setNewPatient({...newPatient, address: e.target.value})}
                required
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowPatientModal(false)}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              Create Patient (+ Auto Billing Account)
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>

      {/* Doctor Modal */}
      <Modal show={showDoctorModal} onHide={() => setShowDoctorModal(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Add New Doctor</Modal.Title>
        </Modal.Header>
        <Form onSubmit={createDoctor}>
          <Modal.Body>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Full Name</Form.Label>
                  <Form.Control
                    type="text"
                    value={newDoctor.name}
                    onChange={(e) => setNewDoctor({...newDoctor, name: e.target.value})}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    value={newDoctor.email}
                    onChange={(e) => setNewDoctor({...newDoctor, email: e.target.value})}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Specialization</Form.Label>
                  <Form.Select
                    value={newDoctor.specialization}
                    onChange={(e) => setNewDoctor({...newDoctor, specialization: e.target.value})}
                    required
                  >
                    <option value="Cardiology">Cardiology</option>
                    <option value="Neurology">Neurology</option>
                    <option value="Pediatrics">Pediatrics</option>
                    <option value="Orthopedics">Orthopedics</option>
                    <option value="Dermatology">Dermatology</option>
                    <option value="General Medicine">General Medicine</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Department</Form.Label>
                  <Form.Control
                    type="text"
                    value={newDoctor.department}
                    onChange={(e) => setNewDoctor({...newDoctor, department: e.target.value})}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Phone Number</Form.Label>
                  <Form.Control
                    type="tel"
                    value={newDoctor.phoneNumber}
                    onChange={(e) => setNewDoctor({...newDoctor, phoneNumber: e.target.value})}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>License Number</Form.Label>
                  <Form.Control
                    type="text"
                    value={newDoctor.licenseNumber}
                    onChange={(e) => setNewDoctor({...newDoctor, licenseNumber: e.target.value})}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Experience (Years)</Form.Label>
                  <Form.Control
                    type="number"
                    value={newDoctor.experienceYears}
                    onChange={(e) => setNewDoctor({...newDoctor, experienceYears: e.target.value})}
                    min="0"
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Address</Form.Label>
                  <Form.Control
                    type="text"
                    value={newDoctor.address}
                    onChange={(e) => setNewDoctor({...newDoctor, address: e.target.value})}
                  />
                </Form.Group>
              </Col>
            </Row>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowDoctorModal(false)}>
              Cancel
            </Button>
            <Button variant="success" type="submit">
              Create Doctor
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </Container>
  );
}

export default App;