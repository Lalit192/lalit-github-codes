import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Alert } from 'react-bootstrap';
import { directAPI } from '../services/api';

function Doctors() {
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [newDoctor, setNewDoctor] = useState({
    name: '',
    email: '',
    specialization: '',
    phoneNumber: '',
    licenseNumber: '',
    experienceYears: '',
    joinedDate: new Date().toISOString().split('T')[0],
    department: '',
    address: ''
  });

  useEffect(() => {
    loadDoctors();
  }, []);

  const loadDoctors = async () => {
    try {
      const response = await directAPI.doctors.getAll();
      setDoctors(response.data);
    } catch (error) {
      setError('Failed to load doctors');
      console.error('Error loading doctors:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      await directAPI.doctors.create(newDoctor);
      setSuccess('Doctor created successfully!');
      setShowModal(false);
      setNewDoctor({
        name: '',
        email: '',
        specialization: '',
        phoneNumber: '',
        licenseNumber: '',
        experienceYears: '',
        joinedDate: new Date().toISOString().split('T')[0],
        department: '',
        address: ''
      });
      loadDoctors();
    } catch (error) {
      setError('Failed to create doctor');
      console.error('Error creating doctor:', error);
    }
  };

  const handleChange = (e) => {
    setNewDoctor({
      ...newDoctor,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div>
      <Row className="mb-4">
        <Col>
          <h1>👨⚕️ Doctors Management</h1>
        </Col>
        <Col xs="auto">
          <Button variant="success" onClick={() => setShowModal(true)}>
            ➕ Add New Doctor
          </Button>
        </Col>
      </Row>

      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}

      <Card>
        <Card.Header>
          <h5>All Doctors ({doctors.length})</h5>
        </Card.Header>
        <Card.Body>
          {loading ? (
            <div className="text-center">Loading doctors...</div>
          ) : (
            <Table striped bordered hover responsive>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Specialization</th>
                  <th>Department</th>
                  <th>Experience</th>
                  <th>Phone</th>
                  <th>License</th>
                  <th>Actions</th>
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
                    <td>
                      <Button variant="outline-success" size="sm" className="me-2">
                        View
                      </Button>
                      <Button variant="outline-secondary" size="sm">
                        Edit
                      </Button>
                    </td>
                  </tr>
                ))}
                {doctors.length === 0 && (
                  <tr>
                    <td colSpan="7" className="text-center">
                      No doctors found. Add your first doctor!
                    </td>
                  </tr>
                )}
              </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>

      {/* Add Doctor Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Add New Doctor</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Full Name</Form.Label>
                  <Form.Control
                    type="text"
                    name="name"
                    value={newDoctor.name}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={newDoctor.email}
                    onChange={handleChange}
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
                    name="specialization"
                    value={newDoctor.specialization}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Select Specialization</option>
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
                    name="department"
                    value={newDoctor.department}
                    onChange={handleChange}
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
                    name="phoneNumber"
                    value={newDoctor.phoneNumber}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>License Number</Form.Label>
                  <Form.Control
                    type="text"
                    name="licenseNumber"
                    value={newDoctor.licenseNumber}
                    onChange={handleChange}
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
                    name="experienceYears"
                    value={newDoctor.experienceYears}
                    onChange={handleChange}
                    min="0"
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Joined Date</Form.Label>
                  <Form.Control
                    type="date"
                    name="joinedDate"
                    value={newDoctor.joinedDate}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>

            <Form.Group className="mb-3">
              <Form.Label>Address</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="address"
                value={newDoctor.address}
                onChange={handleChange}
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Cancel
            </Button>
            <Button variant="success" type="submit">
              Create Doctor
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </div>
  );
}

export default Doctors;