import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { vi } from 'vitest';

// Mock Sidebar to keep Layout isolated
vi.mock('../Sidebar', () => ({
  Sidebar: () => <div data-testid="mock-sidebar">sidebar</div>
}));

import { Layout } from '../Layout';

describe('Layout', () => {
  it('renders header, sidebar and outlet content', () => {
    render(
      <MemoryRouter initialEntries={["/profile"]}>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/profile" element={<div data-testid="outlet">profile</div>} />
          </Route>
        </Routes>
      </MemoryRouter>
    );

    expect(screen.getByTestId('mock-sidebar')).toBeInTheDocument();
    expect(screen.getByTestId('outlet')).toHaveTextContent('profile');
    // header contains app title
    expect(screen.getByText(/Student Tracker/i)).toBeInTheDocument();
  });
});
