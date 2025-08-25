import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Button } from 'react-bootstrap';
import { directAPI } from '../services/api';

function Dashboard() {
  const [stats, setStats] = useState({
    patients: 0,
    doctors: 0,
    appointments: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const [patientsRes, doctorsRes] = await Promise.all([
        directAPI.patients.getAll(),
        directAPI.doctors.getAll()
      ]);

      setStats({
        patients: patientsRes.data.length || 0,
        doctors: doctorsRes.data.length || 0,
        appointments: 0 // Will be updated when appointments are loaded
      });
    } catch (error) {
      console.error('Error loading stats:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 className="mb-4">🏥 Hospital Dashboard</h1>
      
      <Row className="mb-4">
        <Col md={4}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>👥 Patients</Card.Title>
              <h2 className="text-primary">{loading ? '...' : stats.patients}</h2>
              <Card.Text>Total registered patients</Card.Text>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={4}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>👨‍⚕️ Doctors</Card.Title>
              <h2 className="text-success">{loading ? '...' : stats.doctors}</h2>
              <Card.Text>Available doctors</Card.Text>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={4}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>📅 Appointments</Card.Title>
              <h2 className="text-warning">{loading ? '...' : stats.appointments}</h2>
              <Card.Text>Scheduled appointments</Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col md={12}>
          <Card>
            <Card.Header>
              <h5>Quick Actions</h5>
            </Card.Header>
            <Card.Body>
              <Row>
                <Col md={3}>
                  <Button variant="primary" className="w-100 mb-2" href="/patients">
                    👥 Manage Patients
                  </Button>
                </Col>
                <Col md={3}>
                  <Button variant="success" className="w-100 mb-2" href="/doctors">
                    👨‍⚕️ Manage Doctors
                  </Button>
                </Col>
                <Col md={3}>
                  <Button variant="warning" className="w-100 mb-2" href="/appointments">
                    📅 Book Appointment
                  </Button>
                </Col>
                <Col md={3}>
                  <Button variant="info" className="w-100 mb-2" onClick={loadStats}>
                    🔄 Refresh Stats
                  </Button>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="mt-4">
        <Col md={12}>
          <Card>
            <Card.Header>
              <h5>System Status</h5>
            </Card.Header>
            <Card.Body>
              <div className="d-flex justify-content-between">
                <span>🟢 Auth Service</span>
                <span>🟢 Patient Service</span>
                <span>🟢 Doctor Service</span>
                <span>🟢 Billing Service</span>
                <span>🟢 Analytics Service</span>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
}

export default Dashboard;