import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Alert } from 'react-bootstrap';
import { directAPI } from '../services/api';

function Patients() {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [newPatient, setNewPatient] = useState({
    name: '',
    email: '',
    dateOfBirth: '',
    address: '',
    registeredDate: new Date().toISOString().split('T')[0]
  });

  useEffect(() => {
    loadPatients();
  }, []);

  const loadPatients = async () => {
    try {
      const response = await directAPI.patients.getAll();
      setPatients(response.data);
    } catch (error) {
      setError('Failed to load patients');
      console.error('Error loading patients:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      await directAPI.patients.create(newPatient);
      setSuccess('Patient created successfully!');
      setShowModal(false);
      setNewPatient({
        name: '',
        email: '',
        dateOfBirth: '',
        address: '',
        registeredDate: new Date().toISOString().split('T')[0]
      });
      loadPatients();
    } catch (error) {
      setError('Failed to create patient');
      console.error('Error creating patient:', error);
    }
  };

  const handleChange = (e) => {
    setNewPatient({
      ...newPatient,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div>
      <Row className="mb-4">
        <Col>
          <h1>👥 Patients Management</h1>
        </Col>
        <Col xs="auto">
          <Button variant="primary" onClick={() => setShowModal(true)}>
            ➕ Add New Patient
          </Button>
        </Col>
      </Row>

      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}

      <Card>
        <Card.Header>
          <h5>All Patients ({patients.length})</h5>
        </Card.Header>
        <Card.Body>
          {loading ? (
            <div className="text-center">Loading patients...</div>
          ) : (
            <Table striped bordered hover responsive>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Date of Birth</th>
                  <th>Address</th>
                  <th>Registered Date</th>
                  <th>Actions</th>
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
                    <td>
                      <Button variant="outline-primary" size="sm" className="me-2">
                        View
                      </Button>
                      <Button variant="outline-secondary" size="sm">
                        Edit
                      </Button>
                    </td>
                  </tr>
                ))}
                {patients.length === 0 && (
                  <tr>
                    <td colSpan="6" className="text-center">
                      No patients found. Add your first patient!
                    </td>
                  </tr>
                )}
              </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>

      {/* Add Patient Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add New Patient</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Full Name</Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={newPatient.name}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={newPatient.email}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Date of Birth</Form.Label>
              <Form.Control
                type="date"
                name="dateOfBirth"
                value={newPatient.dateOfBirth}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Address</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="address"
                value={newPatient.address}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Registration Date</Form.Label>
              <Form.Control
                type="date"
                name="registeredDate"
                value={newPatient.registeredDate}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              Create Patient
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </div>
  );
}

export default Patients;