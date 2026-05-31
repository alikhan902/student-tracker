import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { PrivateRoute } from '../PrivateRoute';

describe('PrivateRoute', () => {
  it('redirects to login when no token', () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('token');
    render(
      <MemoryRouter initialEntries={["/"]}>
        <Routes>
          <Route element={<PrivateRoute />}>
            <Route path="/" element={<div>Protected</div>} />
          </Route>
          <Route path="/login" element={<div>Login</div>} />
        </Routes>
      </MemoryRouter>
    );
    expect(screen.getByText('Login')).toBeTruthy();
  });
});
