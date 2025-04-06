import React from 'react';

const DownloadAppPage = () => {
  return (
    <div style={styles.container}>
      <h1 style={styles.heading}>Download the SustainCity App</h1>
      <p style={styles.text}>Click the button below to download the Android app:</p>
      <a
        href="https://drive.google.com/file/d/10K0Vx-tVvryp93E2bAQFO0zL9_Ggj47v/view?usp=drivesdk"
        target="_blank"
        rel="noopener noreferrer"
        style={styles.button}
      >
        Download the App
      </a>
    </div>
  );
};

const styles = {
  container: {
    textAlign: 'center',
    padding: '60px',
    fontFamily: 'Arial, sans-serif',
  },
  heading: {
    fontSize: '2.5rem',
    marginBottom: '20px',
  },
  text: {
    fontSize: '1.2rem',
    marginBottom: '30px',
  },
  button: {
    display: 'inline-block',
    padding: '14px 28px',
    backgroundColor: '#b3cf99',
    color: '#fff',
    fontSize: '18px',
    borderRadius: '8px',
    textDecoration: 'none',
    transition: 'background-color 0.3s',
  },
};

export default DownloadAppPage;
