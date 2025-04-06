
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './MainPage.jsx';       
import FullWebsite from './FullWebsite.jsx';
import DownloadAppPage from './DownloadAppPage.jsx'; 

function App() {
  return (
    <Router>
      <Routes>
        
        <Route path="/" element={<MainPage />} />

        <Route path="/download-app" element={<DownloadAppPage />} />
        <Route path="/website/*" element={<FullWebsite />} />
        
      </Routes>
    </Router>
  );
}

export default App;
