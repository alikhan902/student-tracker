import { Routes, Route, Navigate } from 'react-router-dom';
import { Layout } from './components/layout/Layout';
import { PrivateRoute } from './components/auth/PrivateRoute';

// Pages
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import ForgotPasswordPage from './pages/ForgotPasswordPage';
import ProfilePage from './pages/ProfilePage';

// IMPORTANT: Add this import
import OAuthRedirect from './pages/OAuthRedirect';
import SubjectSelectionPage from './pages/SubjectsSelectionPage';
import SubjectPage from './pages/SubjectPage';
import GroupPage from './pages/GroupPage';

function App() {
  return (<Routes>
    {/* Public Auth Routes */}
    <Route path="/login" element={<LoginPage />} />
    <Route path="/signup" element={<SignupPage />} />
    <Route path="/forgot-password" element={<ForgotPasswordPage />} />

    {/* ✅ OAuth route MUST be public and ABOVE PrivateRoute */}
    <Route path="/oauth2/redirect" element={<OAuthRedirect />} />

    {/* Protected Routes */}
    <Route element={<PrivateRoute />}>
      <Route element={<Layout />}>
        <Route path="/profile" element={<ProfilePage />} />
      </Route>
    </Route>
    
    <Route element={<PrivateRoute />}>
      <Route element={<Layout />}>
        <Route path="/subjects" element={<SubjectSelectionPage />} />
      </Route>
    </Route>

    <Route element={<PrivateRoute />}>
      <Route element={<Layout />}>
        <Route path="/subjects/:id" element={<SubjectPage />} />
      </Route>
    </Route>

    <Route element={<PrivateRoute />}>
      <Route element={<Layout />}>
        <Route path="/groups" element={<GroupPage />} />
      </Route>
    </Route>

        <Route element={<PrivateRoute />}>
      <Route element={<Layout />}>
        <Route path="/groups" element={<GroupPage />} />
      </Route>
    </Route>

    {/* Fallback */}
    <Route path="*" element={<Navigate to="/profile" replace />} />
  </Routes>


  );
}

export default App;
