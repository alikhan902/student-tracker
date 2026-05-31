import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';

vi.mock('../../../context/AuthContext', () => ({
  useAuth: () => ({ user: { name: 'Ivan', username: 'ivan' }, logout: vi.fn() })
}));

import { Sidebar } from '../Sidebar';
import { MemoryRouter } from 'react-router-dom';

describe('Sidebar', () => {
  it('renders user initials and links', () => {
    render(<MemoryRouter><Sidebar isOpen={true} setMobileMenuOpen={() => {}} /></MemoryRouter>);
    expect(screen.getByText('Список групп')).toBeInTheDocument();
    expect(screen.getByText('Ivan')).toBeInTheDocument();
  });
});
